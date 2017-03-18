package org.zhenchao.passport.oauth.crypt;

import org.zhenchao.passport.oauth.exception.CryptException;

/**
 * symmetrical encipher
 *
 * @author zhenchao.wang 2017-02-14 10:17
 * @version 1.0.0
 */
public abstract class SymmetricalEncipher {

    /**
     * generate key
     *
     * @return
     */
    public abstract byte[] generateKey();

    /**
     * encrypt
     *
     * @param key
     * @param data
     * @return
     * @throws CryptException
     */
    public abstract byte[] encrypt(byte[] key, byte[] data) throws CryptException;

    /**
     * decrypt
     *
     * @param key
     * @param data
     * @return
     * @throws CryptException
     */
    public abstract byte[] decrypt(byte[] key, byte[] data) throws CryptException;
}
