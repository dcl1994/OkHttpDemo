package com.yyw.okhttpdemo.progressbar;

import java.io.FileNotFoundException;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.X509KeyManager;


/**
 * Created by yyw on 2016/1/21.
 */
public class MyX509KeyManager implements X509KeyManager {
    private X509KeyManager defaultKeyManager;
    private KeyStore _ks;
    private String _password;

    public MyX509KeyManager(KeyStore ks, String password,KeyManager[] keyManagers) {
        _ks = ks;
        _password = password;
        for (KeyManager keyManager : keyManagers){
            if (keyManager instanceof X509KeyManager){
                defaultKeyManager = (X509KeyManager) keyManager;
            }
        }
    }
    public String chooseClientAlias(String[] str, Principal[] principal, Socket socket) {
//        String a = defaultKeyManager.chooseClientAlias(str,principal,socket);
//        PrivateKey p = getPrivateKey(a);
//        PrivateKey p1 = defaultKeyManager.getPrivateKey(a);
//        if (p1!=null && p1.equals(p)){
//            System.out.println("p1.equals(p)");
//        }
        return defaultKeyManager.chooseClientAlias(str,principal,socket);
    }

    public String chooseServerAlias(String str, Principal[] principal, Socket socket) {

        return defaultKeyManager.chooseServerAlias(str, principal, socket);
    }

    public X509Certificate[] getCertificateChain(String alias) {
        try {
            java.security.cert.Certificate[] certificates = this._ks.getCertificateChain(alias);

            if(certificates == null){throw new FileNotFoundException("no certificate found for alias:" + alias);}
            X509Certificate[] x509Certificates = new X509Certificate[certificates.length];
            System.arraycopy(certificates, 0, x509Certificates, 0, certificates.length);
//            X509Certificate[] x509Certificates1 = defaultKeyManager.getCertificateChain(alias);
//            if (x509Certificates1!=null&&x509Certificates1.length>0){
//                if (x509Certificates[0].equals(x509Certificates1[0])){
//                    System.out.println("ok");
//                }
//            }
            return defaultKeyManager.getCertificateChain(alias);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getClientAliases(String str, Principal[] principal) {
        return defaultKeyManager.getClientAliases(str,principal);
    }

    public PrivateKey getPrivateKey(String alias) {
//        try {
//            return (PrivateKey) _ks.getKey(alias, _password == null ? null : _password.toCharArray());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        try {
            return defaultKeyManager.getPrivateKey(alias);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getServerAliases(String str, Principal[] principal) {
        String[] a = defaultKeyManager.getServerAliases(str, principal);
        return defaultKeyManager.getServerAliases(str,principal);
    }
}
