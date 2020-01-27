package com.keer.BDQLParser;


import com.bigchaindb.model.Asset;
import com.bigchaindb.model.Assets;
import com.bigchaindb.model.Transaction;
import com.bigchaindb.util.KeyPairUtils;

import com.keer.Pojo.BigchaindbData;
import com.keer.Pojo.MetaData;
import com.keer.Pojo.ParserResult;
import com.keer.Pojo.Table;
import com.keer.Util.BigchainDBUtil;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.util.*;

/**
 * @BelongsProject: BDQLGUI
 * @BelongsPackage: BDQLParser
 * @Author: keer
 * @CreateTime: 2020-01-16 15:25
 * @Description:
 */
public class BDQL {

    private static Logger logger = LoggerFactory.getLogger(BDQL.class);

    public ParserResult work(String sql) {
        ParserResult result = new ParserResult();
        String SQL = lowercaseToUpperCase(sql);
        if (checkChar(SQL)) {
            logger.info("BDQL解析第一步(成功)：转换大小写，检查是否有违禁字符串：；");
            return BDQLParser(SQL);
        } else {
            logger.info("BDQL解析第一步(失败)：转换大小写，检查是否有违禁字符串：\'；\'");
            result.setStatus(ParserResult.ERROR);
            result.setMessage("BDQL解析第一步(失败)：转换大小写，检查是否有违禁字符串：；");
            return result;

        }
    }

    /**
     * 将字符串装换为大写
     *
     * @param bdql
     * @return
     */
    private String lowercaseToUpperCase(String bdql) {
        StringBuffer s = new StringBuffer();
        char c[] = bdql.toCharArray();
        for (int i = 0; i < bdql.length(); i++) {

            if (c[i] >= 97 && c[i] <= 122) {
                s.append((c[i] + "").toUpperCase());
            } else {
                s.append(c[i]);
            }
        }
        logger.info("字符串：" + bdql + ",转换后：" + s);
        return s.toString();
    }

    /**
     * 检查是否有违规字符：禁止使用‘；’
     *
     * @param sql
     * @return 没有返回ture，有返回false
     */
    private boolean checkChar(String sql) {
        if (sql.indexOf(";") == -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 开始解析BDQL
     *
     * @param BDQL
     * @return
     */
    private ParserResult BDQLParser(String BDQL) {
        ParserResult result = new ParserResult();
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(BDQL);
        } catch (JSQLParserException e) {
            logger.info("BDQL解析第二步：（失败）分析不同的sql类型，insert，update，select");
            logger.error("BDQL解析第二步原因：" + getJSQLParserExceptionCauseBy(e));
            e.printStackTrace();
        }
        if (statement instanceof Select) {
            logger.info("BDQL解析第二步：（成功）分析不同的sql类型，insert，update，select，解析结果为select");
            return selectParser((Select) statement);
        } else if (statement instanceof Insert) {
            logger.info("BDQL解析第二步：（成功）分析不同的sql类型，insert，update，select，解析结果为insert");
            return insertParser((Insert) statement);
        } else if (statement instanceof Update) {
            logger.info("BDQL解析第二步：（成功）分析不同的sql类型，insert，update，select，解析结果为Update");
            return updateParser((Update) statement);
        } else {
            logger.warn("BDQL解析第二步：（失败）分析不同的sql类型，insert，update，select,其他语法暂未支持！！！");
            result.setMessage("BDQL解析第二步：（失败）分析不同的sql类型，insert，update，select,其他语法暂未支持！！！");
            result.setStatus(ParserResult.ERROR);
            return result;
        }
    }

    /**
     * 获得JSqlParser的报错信息，并打印。
     *
     * @param e
     * @return
     */
    private String getJSQLParserExceptionCauseBy(JSQLParserException e) {
        String[] ss = e.getCause().getMessage().split(":");
        String s = ss[1].replace("\n", "");
        String result = s.replace("Was expecting one of", "");
        logger.error("BDQL语法错误原因，正在查找……………………");
        return result;
    }

    /**
     * 查询语句的解析
     *
     * @param select
     * @return
     */
    private ParserResult selectParser(Select select) {
        ParserResult result = new ParserResult();
        Table table = new Table();
        BigchainDBUtil bigchainDBUtil = new BigchainDBUtil();

        PlainSelect selectBody = (PlainSelect) select.getSelectBody();
        //获取where后面的表达式
        EqualsTo expression = (EqualsTo) selectBody.getWhere();

        // 获取表名
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableNames = tablesNamesFinder.getTableList(select);

        if (tableNames.size() == 1) {
            table.setTableName(tableNames.get(0));
            //获得列名
            ArrayList<String> columnNames = (ArrayList<String>) getColumnNames(selectBody);
            if (expression.getLeftExpression().toString().equals("ID")) {//表达式左边是交易ID,where表达式'ID=0x123123'
                String TXID = expression.getRightExpression().toString();
                Transaction transaction = bigchainDBUtil.getTransactionByTXID(TXID);
                if (transaction.getOperation().equals("CREATE")) {
                    table.setType("CREATE");
                    Assets assets = new Assets();
                    Asset asset = transaction.getAsset();
                    assets.addAsset(asset);
                    table.setTableData(assets);
                } else {
                    table.setType("TRANSFER");
                    List<MetaData> metaDatas = new LinkedList<MetaData>();
                    MetaData metaData = (MetaData) transaction.getMetaData();
                    metaDatas.add(metaData);
                    table.setTableData(metaDatas);
                }
            } else {
                if (columnNames.size() == 1 && columnNames.get(0).equals("*")) {
                    if (bigchainDBUtil.getAssetBySearchKey(table.getTableName()).size() == 0) {
                        List<MetaData> metaDatas = bigchainDBUtil.getMetaDatasByKey(table.getTableName());
                        table.setType("TRANSFER");
                        List<MetaData> newMetadatas = selectMetadata(metaDatas, expression);
                        table.setTableDataWithCloumnName(newMetadatas);
                    } else {
                        Assets assets = bigchainDBUtil.getAssetBySearchKey(table.getTableName());
                        table.setType("CREATE");
                        Assets newAssets = selectAssets(assets, expression);
                        table.setTableDataWithColumnName(newAssets);
                    }
                } else {
                    if (bigchainDBUtil.getAssetBySearchKey(table.getTableName()).size() == 0) {
                        List<MetaData> metaDatas = bigchainDBUtil.getMetaDatasByKey(table.getTableName());
                        table.setType("TRANSFER");
                        table.setColumnName(columnNames);
                        List<MetaData> newMetadatas = selectMetadata(metaDatas, expression);
                        table.setTableData(newMetadatas);

                    } else {
                        Assets assets = bigchainDBUtil.getAssetBySearchKey(table.getTableName());
                        table.setType("CREATE");
                        table.setColumnName(columnNames);
                        Assets newAssets = selectAssets(assets, expression);
                        table.setTableData(newAssets);

                    }
                }
            }
            result.setStatus(ParserResult.SUCCESS);
            result.setData(table);
            result.setMessage("select");
            return result;
        } else {
            logger.error("BDQL解析第三步(失败):暂不支持多表查询语法！！");
            result.setStatus(ParserResult.ERROR);
            result.setMessage("BDQL解析第三步(失败):暂不支持多表查询语法！！");
            return result;
        }
    }

    /**
     * 获得select的查询列名
     *
     * @param selectBody
     * @return
     */
    private List<String> getColumnNames(SelectBody selectBody) {
        PlainSelect plainSelect = (PlainSelect) selectBody;
        //获得查询的列名
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        List<String> str_items = new ArrayList<String>();
        if (selectItems != null) {
            for (int i = 0; i < selectItems.size(); i++) {
                str_items.add(selectItems.get(i).toString());
            }
        }
        return str_items;
    }

    /**
     * 根据where挑选asset
     *
     * @param assets
     * @param expression
     * @return
     */
    private static Assets selectAssets(Assets assets, Expression expression) {
        if (expression == null) {
            return assets;
        }
        Assets newAssets = new Assets();
        //等于：=
        if (expression instanceof EqualsTo) {
            String left = ((EqualsTo) expression).getLeftExpression().toString();
            String right = ((EqualsTo) expression).getRightExpression().toString();
            for (Asset asset : assets.getAssets()) {
                Map map = (Map) asset.getData();
                Map map1 = (Map) map.get("tableData");
                if (map1.get(left).toString().equals(right)) {
                    newAssets.addAsset(asset);
                }
            }
        }
        //大于：>
        if (expression instanceof GreaterThan) {
            String left = ((GreaterThan) expression).getLeftExpression().toString();
            String right = ((GreaterThan) expression).getRightExpression().toString();
            int R = Integer.parseInt(right);
            for (Asset asset : assets.getAssets()) {
                Map map = (Map) asset.getData();
                Map map1 = (Map) map.get("tableData");
                if (Integer.parseInt(map1.get(left).toString()) > R) {
                    newAssets.addAsset(asset);
                }
            }
        }
        //大于等于：>=
        if (expression instanceof GreaterThanEquals) {
            String left = ((GreaterThanEquals) expression).getLeftExpression().toString();
            String right = ((GreaterThanEquals) expression).getRightExpression().toString();
            int R = Integer.parseInt(right);
            for (Asset asset : assets.getAssets()) {
                Map map = (Map) asset.getData();
                Map map1 = (Map) map.get("tableData");
                if (Integer.parseInt(map1.get(left).toString()) >= R) {
                    newAssets.addAsset(asset);
                }
            }
        }
        //小于：<
        if (expression instanceof MinorThan) {
            String left = ((MinorThan) expression).getLeftExpression().toString();
            String right = ((MinorThan) expression).getRightExpression().toString();
            int R = Integer.parseInt(right);
            for (Asset asset : assets.getAssets()) {
                Map map = (Map) asset.getData();
                Map map1 = (Map) map.get("tableData");
                if (Integer.parseInt(map1.get(left).toString()) < R) {
                    newAssets.addAsset(asset);
                }
            }
        }
        //小于等于：<=
        if (expression instanceof MinorThanEquals) {
            String left = ((MinorThanEquals) expression).getLeftExpression().toString();
            String right = ((MinorThanEquals) expression).getRightExpression().toString();
            int R = Integer.parseInt(right);
            for (Asset asset : assets.getAssets()) {
                Map map = (Map) asset.getData();
                Map map1 = (Map) map.get("tableData");
                if (Integer.parseInt(map1.get(left).toString()) <= R) {
                    newAssets.addAsset(asset);
                }
            }
        }
        return newAssets;

    }

    /**
     * 根据where挑选metadata
     *
     * @param metaDataList
     * @param expression
     * @return
     */
    private static List<MetaData> selectMetadata(List<MetaData> metaDataList, Expression expression) {
        if (expression == null) {
            return metaDataList;
        }
        List<MetaData> newMetadata = new ArrayList<MetaData>();
        //等于号：=
        if (expression instanceof EqualsTo) {
            String left = ((EqualsTo) expression).getLeftExpression().toString();
            String right = ((EqualsTo) expression).getRightExpression().toString();
            for (MetaData metaData : metaDataList) {
                Map map = metaData.getMetadata();
                Map map1 = (Map) map.get("tableData");
                if (map1.get(left).toString().equals(right)) {
                    newMetadata.add(metaData);
                }
            }
        }
        //大于号 ：>
        if (expression instanceof GreaterThan) {
            String left = ((GreaterThan) expression).getLeftExpression().toString();
            String right = ((GreaterThan) expression).getRightExpression().toString();
            int R = Integer.parseInt(right);
            for (MetaData metaData : metaDataList) {
                Map map = metaData.getMetadata();
                Map map1 = (Map) map.get("tableData");
                if (Integer.parseInt(map1.get(left).toString()) > R) {
                    newMetadata.add(metaData);
                }
            }
        }
        //大于等于号：>=
        if (expression instanceof GreaterThanEquals) {
            String left = ((GreaterThanEquals) expression).getLeftExpression().toString();
            String right = ((GreaterThanEquals) expression).getRightExpression().toString();
            int R = Integer.parseInt(right);
            for (MetaData metaData : metaDataList) {
                Map map = metaData.getMetadata();
                Map map1 = (Map) map.get("tableData");
                if (Integer.parseInt(map1.get(left).toString()) >= R) {
                    newMetadata.add(metaData);
                }
            }

        }
        //小于号：<
        if (expression instanceof MinorThan) {
            String left = ((MinorThan) expression).getLeftExpression().toString();
            String right = ((MinorThan) expression).getRightExpression().toString();
            int R = Integer.parseInt(right);
            for (MetaData metaData : metaDataList) {
                Map map = metaData.getMetadata();
                Map map1 = (Map) map.get("tableData");
                if (Integer.parseInt(map1.get(left).toString()) < R) {
                    newMetadata.add(metaData);
                }
            }

        }
        //小于号：<=
        if (expression instanceof MinorThanEquals) {
            String left = ((MinorThanEquals) expression).getLeftExpression().toString();
            String right = ((MinorThanEquals) expression).getRightExpression().toString();
            int R = Integer.parseInt(right);
            for (MetaData metaData : metaDataList) {
                Map map = metaData.getMetadata();
                Map map1 = (Map) map.get("tableData");
                if (Integer.parseInt(map1.get(left).toString()) <= R) {
                    newMetadata.add(metaData);
                }
            }

        }
        return newMetadata;

    }

    /**
     * 开始解析insert语句
     *
     * @param insert Insert
     */
    private ParserResult insertParser(Insert insert) {
        ParserResult result = new ParserResult();
        BigchainDBUtil bigchainDBUtil = new BigchainDBUtil();
        //表名
        String tableName = insert.getTable().getName();
        //字段名
        List<Column> colums = insert.getColumns();
        //字段值
        ExpressionList expressionList = (ExpressionList) insert.getItemsList();
        List<Expression> values = expressionList.getExpressions();

        Map map = toMap(colums, values);
        BigchaindbData data = new BigchaindbData(tableName, map);

        String id = null;
        try {
            id = bigchainDBUtil.createAsset(data, null);
            logger.info("BDQL解析第三步(成功):插入操作成功！！！！！");
            result.setStatus(ParserResult.SUCCESS);
            result.setMessage("插入操作成功！！");
            result.setData(id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("BDQL解析第三步(失败):插入操作失败！！！！！");
            result.setStatus(ParserResult.ERROR);
            result.setMessage("插入操作失败！！");
        }
        return result;
    }


    /**
     * 将两个list 合并到一个map中
     *
     * @param key
     * @param value
     * @return
     */
    private Map toMap(List key, List value) {
        Map<String, String> map = new HashMap();
        for (int i = 0; i < key.size(); i++) {
            String sk = fixString(key.get(i).toString());
            String sv = fixString(value.get(i).toString());
            map.put(sk, sv);
        }
        return map;
    }


    /**
     * 去掉字符串的第一个和最后一个单引号
     *
     * @param s
     * @return
     */
    private String fixString(String s) {
        if (s.substring(0, 1).equals("'") && s.substring(s.length() - 1, s.length()).equals("'")) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    private ParserResult updateParser(Update update) {
        ParserResult result = new ParserResult();
        BigchainDBUtil bigchainDBUtil = new BigchainDBUtil();

        AndExpression andExpression = (AndExpression) update.getWhere();
        EqualsTo left = (EqualsTo) andExpression.getLeftExpression();
        EqualsTo right = (EqualsTo) andExpression.getRightExpression();

        String ID = left.getLeftExpression().toString();
        String values = left.getRightExpression().toString();
        values = values.substring(1, values.length() - 1);

        String key = right.getLeftExpression().toString();
        String keyValue = right.getRightExpression().toString();
        keyValue = keyValue.substring(1, values.length() - 1);

        //表名
        String tableName = update.getTables().get(0).getName();

        //字段名
        List<Column> colums = update.getColumns();

        //字段值
        List<Expression> expressions = update.getExpressions();

        BigchaindbData data = new BigchaindbData(tableName, toMap(colums, expressions));
        String id = null;
        if (right == null) {
            if (ID.equals("ID")) {
                try {
                    id = bigchainDBUtil.transferToSelf(data, values);
                    logger.info("BDQL解析第三步(成功)：更新成功！！！");
                } catch (Exception e) {
                    logger.error("BDQL解析第三步(失败):BDQL语法错误：where 只能使用ID='' and TOPUBLIC=''，请检查书写和大小写");
                    result.setMessage("BDQL语法错误：where 只能使用ID=————，请检查书写和大小写");
                    result.setStatus(ParserResult.ERROR);
                    return result;
                }
            } else {
                logger.error("BDQL解析第三步(失败):BDQL语法错误：where 只能使用ID='' and TOPUBLIC=''，请检查书写和大小写");
                result.setMessage("BDQL语法错误：where 只能使用ID=————，请检查书写和大小写");
                result.setStatus(ParserResult.ERROR);
                return result;
            }

        } else {
            if (ID.equals("ID") && key.equals("TOPUBLIC")) {
                KeyPair keyPair = KeyPairUtils.decodeKeyPair(key);
                id = bigchainDBUtil.transfer(data, values, (EdDSAPublicKey) keyPair.getPublic());
                logger.info("BDQL解析第三步(成功)：更新成功！！！");
            } else {
                logger.error("BDQL解析第三步(失败):BDQL语法错误：where 只能使用ID='' and TOPUBLIC=''，请检查书写和大小写");
                result.setMessage("BDQL语法错误：where 只能使用ID=————，请检查书写和大小写");
                result.setStatus(ParserResult.ERROR);
                return result;
            }
        }
        //检查交易是否成功
        if (bigchainDBUtil.checkTransactionExit(id)) {
            logger.info("BDQL解析第四步(成功)：数据库插入成功！更新成功！！！id：" + id);
            result.setMessage("BDQL解析第四步(成功)：数据库插入成功！更新成功！！！");
            result.setStatus(ParserResult.SUCCESS);
            result.setData(id);
            return result;
        } else {
            logger.error("BDQL解析第四步(失败)：更新失败，数据库操作失败！！！");
            result.setMessage("BDQL解析第四步(失败)：数据库操作失败！！！");
            result.setStatus(ParserResult.ERROR);
            return result;
        }
    }

    public static void main(String[] args) {
        Update update = new Update();
        update.getWhere();
    }

}
