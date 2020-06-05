/*
 * Copyright 2013 Google Inc.
 * Copyright 2015 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.params;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.utils.MonetaryFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bitcoinj.msg.BitcoinSerializer;

/**
 * Parameters for Bitcoin-like networks.
 */
public abstract class AbstractBitcoinNetParams extends NetworkParameters {
    /**
     * Scheme part for Bitcoin URIs.
     */
    public static final String BITCOIN_SCHEME = "bitcoin";

    private static final Logger log = LoggerFactory.getLogger(AbstractBitcoinNetParams.class);

    // Aug, 1 hard fork
    int uahfHeight = 478559;

    public AbstractBitcoinNetParams(Network network) {
        super(network);
    }

    ///**
// * To reduce the impact of timestamp manipulation, we select the block we are
// * basing our computation on via a median of 3.
// */
//    StoredBlock GetSuitableBlock(StoredBlock storedBlock, BlockStore blockStore) throws BlockStoreException{
//        Preconditions.checkState(storedBlock.getHeight() >= 3);
//
//        /**
//         * In order to avoid a block is a very skewed timestamp to have too much
//         * influence, we select the median of the 3 top most blocks as a starting
//         * point.
//         */
//        StoredBlock blocks[] = new StoredBlock[3];
//        blocks[2] = storedBlock;
//        blocks[1] = storedBlock.getPrev(blockStore);
//        if(blocks[1] == null)
//            throw new BlockStoreException("Not enough blocks in blockStore to calculate difficulty");
//        blocks[0] = blocks[1].getPrev(blockStore);
//        if(blocks[0] == null)
//            throw new BlockStoreException("Not enough blocks in blockStore to calculate difficulty");
//
//        // Sorting network.
//        if (blocks[0].getHeader().getTimeSeconds() > blocks[2].getHeader().getTimeSeconds()) {
//            StoredBlock temp = blocks[0];
//            blocks[0] = blocks[2];
//            blocks[2] = temp;
//        }
//
//        if (blocks[0].getHeader().getTimeSeconds() > blocks[1].getHeader().getTimeSeconds()) {
//            StoredBlock temp = blocks[0];
//            blocks[0] = blocks[1];
//            blocks[1] = temp;
//        }
//
//        if (blocks[1].getHeader().getTimeSeconds() > blocks[2].getHeader().getTimeSeconds()) {
//            StoredBlock temp = blocks[1];
//            blocks[1] = blocks[2];
//            blocks[2] = temp;
//        }
//
//        // We should have our candidate in the middle now.
//        return blocks[1];
//    }

//    /**
//     * Compute the next required proof of work using a weighted average of the
//     * estimated hashrate per block.
//     *
//     * Using a weighted average ensure that the timestamp parameter cancels out in
//     * most of the calculation - except for the timestamp of the first and last
//     * block. Because timestamps are the least trustworthy information we have as
//     * input, this ensures the algorithm is more resistant to malicious inputs.
//     */
//    protected void checkNextCashWorkRequired(StoredBlock storedPrev,
//                                             Block newBlock, BlockStore blockStore) {
//
//        // Compute the difficulty based on the full adjustment interval.
//        int height = storedPrev.getHeight();
//        Preconditions.checkState(height >= this.interval);
//
//        // Get the last suitable block of the difficulty interval.
//        try {
//            StoredBlock lastBlock = GetSuitableBlock(storedPrev, blockStore);
//
//            // Get the first suitable block of the difficulty interval.
//            StoredBlock firstBlock = storedPrev;
//
//            for (int i = 144; i > 0; --i)
//            {
//                firstBlock = firstBlock.getPrev(blockStore);
//                if(firstBlock == null)
//                    return; //Not enough blocks in the blockchain to calculate difficulty
//            }
//
//            firstBlock = GetSuitableBlock(firstBlock, blockStore);
//
//            // Compute the target based on time and work done during the interval.
//            BigInteger nextTarget =
//                    ComputeTarget(firstBlock, lastBlock);
//
//            Verification.verifyDifficulty(this, nextTarget, newBlock);
//        }
//        catch (BlockStoreException x)
//        {
//            //this means we don't have enough blocks, yet.  let it go until we do.
//            return;
//        }
//    }

    @Override
    public Coin getMaxMoney() {
        return MAX_MONEY;
    }

    @Override
    public Coin getMinNonDustOutput() {
        return Coin.MIN_NONDUST_OUTPUT;
    }

    @Override
    public MonetaryFormat getMonetaryFormat() {
        return new MonetaryFormat();
    }

    @Override
    public int getProtocolVersionNum(final ProtocolVersion version) {
        return version.getBitcoinProtocolVersion();
    }

    @Override
    public BitcoinSerializer getSerializer(boolean parseLazy, boolean parseRetain, boolean compactTransactionsInBlock) {
        return new BitcoinSerializer(this, parseLazy, parseRetain, compactTransactionsInBlock);
    }

    public BitcoinSerializer getSerializer(boolean parseLazy, boolean parseRetain) {
        return getSerializer(parseLazy, parseRetain, defaultSerializeMode.isCompactTransactionsInBlock());
    }

    @Override
    public String getUriScheme() {
        return BITCOIN_SCHEME;
    }

    @Override
    public boolean hasMaxMoney() {
        return true;
    }
}
