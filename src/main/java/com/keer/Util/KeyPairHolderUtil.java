package com.keer.Util;

import com.bigchaindb.util.KeyPairUtils;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;

/**
 * @BelongsProject: BDQLGUI
 * @BelongsPackage: Util
 * @Author: keer
 * @CreateTime: 2020-01-14 22:04
 * @Description: BigchainDB密钥管理
 */
public class KeyPairHolderUtil {

    private Logger logger = LoggerFactory.getLogger(KeyPairHolderUtil.class);
    private String keyPath= PropertyUtil.getProperties("keyPath");

    /**
     * 通过指定路径获得密钥对
     *
     * @return 获得固定路径下的秘钥
     */
    public KeyPair getKeyPairFromTXT(String path) {
        try {
            FileInputStream in = new FileInputStream(path);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            String key = new String(buffer);
            return KeyPairUtils.decodeKeyPair(key);
        } catch (Exception e) {
            logger.error("对应路径下没有密钥文件");
            return null;
        }
    }

    //TODO 以下两个方法均是从项目下txt获得密钥，方便测试使用。！！！！之后应该吧路径写入配置文件

    /**
     * 从./keypair.txt中获得发送交易使用的公钥
     *
     * @return
     */
    public EdDSAPublicKey getPublic() {
        return getPublic(keyPath);
    }

    public EdDSAPublicKey getPublic(String path ){
        logger.info("获得"+path+"中的公钥");
        return (EdDSAPublicKey) getKeyPairFromTXT(path).getPublic();
    }

    /**
     * 公钥转换成字符串
     * @param key
     * @return
     */
    public String pubKeyToString(EdDSAPublicKey key){
        return KeyPairUtils.encodePublicKeyInBase58(key);
    }

    /**
     * 从txt获得发送交易使用的私钥
     *
     * @return
     */
    public EdDSAPrivateKey getPrivate() {
        return getPrivate(keyPath);
    }

    /**
     *从指定路径下获得密钥
     * @param path
     * @return
     */
    public EdDSAPrivateKey getPrivate(String path) {
        logger.info("获得"+path+"中的私钥");
        return (EdDSAPrivateKey) getKeyPairFromTXT(path).getPrivate();
    }

    /**
     * 将密钥对存贮在配置文件的指定路径下的文件中
     *
     * @param keyPair
     */
    public boolean SaveKeyPairToTXT(KeyPair keyPair) {
        return SaveKeyPairToTXT(keyPair,keyPath);
    }

    public boolean SaveKeyPairToTXT(KeyPair keyPair,String path) {
        try {
            logger.info("开始写密钥到" + path);
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(KeyPairUtils.encodePrivateKeyBase64(keyPair).getBytes());
            fos.close();
            logger.info("写密钥成功");
            return true;
        } catch (Exception e) {
            logger.error("写密钥失败");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获得密钥对
     *
     * @return
     */
    public  KeyPair getKeyPair() {
        KeyPairGenerator edDsaKpg = new KeyPairGenerator();
        logger.info("成功获取新的密钥对");
        return edDsaKpg.generateKeyPair();
    }

    public static void main(String[] args) {
        //产生新的密钥对并输出到文件
        KeyPairHolderUtil keyPairHolder=new KeyPairHolderUtil();
        keyPairHolder.SaveKeyPairToTXT(keyPairHolder.getKeyPair(),"./keypair.txt");

    }
}
