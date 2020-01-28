package com.keer.Util;

import com.alibaba.fastjson.JSON;
import com.bigchaindb.api.AssetsApi;
import com.bigchaindb.api.OutputsApi;
import com.bigchaindb.api.TransactionsApi;
import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.BigchainDbApi;
import com.bigchaindb.constants.Operations;

import com.bigchaindb.model.*;
import com.bigchaindb.util.NetworkUtils;
import com.google.gson.JsonSyntaxException;

import com.keer.Pojo.BigchaindbData;
import com.keer.Pojo.MetaData;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;


public class BigchainDBUtil {
    private static Logger logger = LoggerFactory.getLogger(BigchainDBUtil.class);


    KeyPairHolderUtil keyPairHolder;

    /**
     * 使用配置文件中的指定路径中的密钥，创建资产数据和metadata数据
     *
     * @param assetDate 资产数据
     * @param metadata
     * @return
     * @throws Exception
     */
    public String createAsset(BigchaindbData assetDate, BigchaindbData metadata) throws Exception {
        return createAsset(assetDate, metadata, keyPairHolder.getPublic(), keyPairHolder.getPrivate());
    }

    /**
     * 通过公私钥创建资产和metadata
     *
     * @param assetWrapper
     * @param metadataWrapper
     * @param publicKey
     * @param privateKey
     * @return
     * @throws Exception
     */
    public String createAsset(Object assetWrapper, Object metadataWrapper, EdDSAPublicKey publicKey, EdDSAPrivateKey privateKey) throws Exception {

        Transaction createTransaction = BigchainDbTransactionBuilder
                .init()
                .operation(Operations.CREATE)
                .addAssets(assetWrapper, assetWrapper.getClass())
                .addMetaData(metadataWrapper)
                .buildAndSign(publicKey, privateKey)
                .sendTransaction();
        return createTransaction.getId();
    }


    /**
     * 使用配置文件中的指定密钥发送交易
     * <p>
     * youself is KeyPairHolder.getKeyPair() representive

     * @param metaData
     * @param assetId
     * @return
     * @throws Exception
     */
    public String transferToSelf(BigchaindbData metaData, String assetId) {
        return transfer(metaData, assetId, keyPairHolder.getPublic(), keyPairHolder.getPrivate(), keyPairHolder.getPublic());
    }

    public  String transfer(BigchaindbData metaData,String asstId,EdDSAPublicKey toPublic){
        return transfer(metaData,asstId,keyPairHolder.getPublic(),keyPairHolder.getPrivate(),toPublic);
    }
    /**
     * 使用密钥发送数据
     *
     * @param metaData
     * @param assetId
     * @param fromPublic
     * @param fromPrivate
     * @param toPublic
     * @return 返回交易id
     */
    public String transfer(Object metaData, String assetId, EdDSAPublicKey fromPublic, EdDSAPrivateKey fromPrivate, EdDSAPublicKey toPublic) {

        Transaction transferTransaction = null;
        try {
            transferTransaction = BigchainDbTransactionBuilder
                    .init()
                    .operation(Operations.TRANSFER)
                    .addAssets(assetId, String.class)
                    .addMetaData(metaData)
                    .addInput(null, transferToSelfFulFill(assetId), fromPublic)
                    .addOutput("1", toPublic)
                    .buildAndSign(fromPublic, fromPrivate)
                    .sendTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("资产ID：" + assetId + ",不存在!!!!!!!");
            return null;
        }
        return transferTransaction.getId();
    }

    /**
     * 通过资产id获取最后交易输出
     *
     * @param assetId
     * @return
     * @throws IOException
     */
    private FulFill transferToSelfFulFill(String assetId) throws IOException, InterruptedException {
        final FulFill spendFrom = new FulFill();
        String transactionId = getLastTransactionId(assetId);
        spendFrom.setTransactionId(transactionId);
        spendFrom.setOutputIndex(0);
        return spendFrom;
    }

    /**
     * 通过资产id获取最后交易id
     *
     * @param assetId asset Id
     * @return last transaction id
     * @throws IOException
     */
    public String getLastTransactionId(String assetId) throws IOException, InterruptedException {
        Transactions transactions = TransactionsApi.getTransactionsByAssetId(assetId, Operations.TRANSFER);
        List<Transaction> transfers = transactions.getTransactions();
//        String json= HttpUtil.httpGet(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.TRANSACTIONS + "/?asset_id=" + assetId + "&operation=TRANSFER");
//        List<Transaction> transfers=JSON.parseArray(json,Transaction.class);

        if (transfers != null && transfers.size() > 0) {
            return transfers.get(transfers.size() - 1).getId();
        } else {
            return assetId;
        }
    }


    /**
     * 通过资产id得到transaction（create）
     *
     * @param assetId
     * @return
     * @throws IOException
     */
    public Transaction getCreateTransaction(String assetId) throws IOException {
        try {
            Transactions apiTransactions = TransactionsApi.getTransactionsByAssetId(assetId, Operations.CREATE);

            List<Transaction> transactions = apiTransactions.getTransactions();
            if (transactions != null && transactions.size() == 1) {
                return transactions.get(0);
            } else {
                return null;
            }

        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * Transaction获取交易id
     *
     * @param transaction
     * @return
     */
    private String getTransactionId(Transaction transaction) {
        String withQuotationId = transaction.getId();
        return withQuotationId.substring(1, withQuotationId.length() - 1);
//        return transaction.getId();
    }

    /**
     * 检查交易是否存在
     *
     * @param txID
     * @return
     */
    public boolean checkTransactionExit(String txID) {
        try {
            Thread.sleep(2000);
            Transaction transaction = TransactionsApi.getTransactionById(txID);
            Thread.sleep(2000);
            if (!transaction.getId().equals(null)) {
                logger.info("交易存在！！ID：" + txID);
                return true;
            } else {
                logger.info("交易不存在！！ID：" + txID);
                return false;
            }
        } catch (Exception e) {
            logger.error("未知错误！！！");
            e.printStackTrace();
            return false;

        }

    }

    /**
     * 通过公钥获得全部交易
     *
     * @param publicKey
     * @return
     * @throws IOException
     */
    public Transactions getAllTransactionByPubKey(String publicKey) throws IOException {
        Transactions transactions = new Transactions();
        Outputs outputs = OutputsApi.getOutputs(publicKey);
        for (Output output : outputs.getOutput()) {
            String assetId = output.getTransactionId();
            Transaction transaction = TransactionsApi.getTransactionById(assetId);
            transactions.addTransaction(transaction);
        }
        return transactions;
    }

    /**
     * 通过key查询资产
     *
     * @param searchKey
     * @return
     */
    public Assets getAssetBySearchKey(String searchKey) {
        try {
            return AssetsApi.getAssets(searchKey);
        } catch (IOException e) {
            logger.error("未知错误！！！！！！！");
            return null;
        }
    }

    /**
     * 通过Key查询metadata
     *
     * @param key
     * @return
     */
    public List<MetaData> getMetaDatasByKey(String key) {
        logger.debug("getMetaData Call :" + key);
        Response response;
        String body = null;
        try {
            response = NetworkUtils.sendGetRequest(BigChainDBGlobals.getBaseUrl() + BigchainDbApi.METADATA + "/?search=" + key);
            body = response.body().string();
            response.close();
        } catch (Exception e) {
            logger.error("未知错误！！！！！！！");
            return null;
        }

        return JSON.parseArray(body, MetaData.class);
    }


    public Transaction getTransactionByTXID(String ID) {
        logger.info("开始查询交易信息：TXID：" + ID);
        try {
            logger.info("查询成功！！！！！！");
            return TransactionsApi.getTransactionById(ID);
        } catch (IOException e) {
            logger.error("交易不存在，TXID：" + ID);
            return null;
        }
    }


    public static void main(String[] args) throws IOException {

//        BigchainDBRunner.StartConn();
//
//        Map<String, Table> result = null;
//        try {
//            result = BDQLUtil.getAlltablesByPubKey(KeyPairHolder.pubKeyToString(KeyPairHolder.getPublic()));
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        Object[] columnNames = result.get("Person").getColumnName().toArray();
//        logger.info(String.valueOf(columnNames.length));
//        List<Map> data = result.get("Person").getData();
//        List<Object[]> objects = new ArrayList<Object[]>();
//        for (Map map : data) {
//            Collection va = map.values();
//
//            Object[] a = va.toArray();
//            objects.add(a);
//        }
//
//        Object[][] b = (Object[][]) objects.toArray(new Object[data.size()][columnNames.length]);
//        logger.info("hhhh");
    }
}