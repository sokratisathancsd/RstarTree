import java.io.*;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;

/**
 * This class is used to load an already created datbase by blocks
 * @author Sokratis Athanasiadis
 * @author Konstantinos Perrakis
 * @version 1.0
 */
public class LoadData {


    private int RecordSize;



    /**
     * This mehod return the total number of entries
     * @return
     */
    public int getRecordSize() {
        return RecordSize;
    }


    /**
     * This method reads the file by accepting 32KB Blocks
     * @return The Arraylist with entries (Points of Interest)
     */
    public ArrayList<Location> ReadClass() {

        ArrayList<Location> locations = new ArrayList<>();
        String strFilePath = "dataBlocks.txt";
        try {
            DataInputStream is = new DataInputStream(new FileInputStream(strFilePath));
            long id = 0;
            double lat = 0, lon = 0;
            byte[] myBufferMeta = new byte[20];// The first block with metadata is 20 Bytes
            is.read(myBufferMeta, 0, 20);       // We fetch the first block
            byte BlockNum[] = new byte[4];             // The 1st Block Contains three variables the  number of blocks , the total Number of records , and the number of records inside the block
            byte Tolatrecords[] = new byte[4];
            byte RecordsInBlock[] = new byte[4];
            int a = 0;
            int n = 0;
            int j=0;
            for(int cnt=8;cnt<20;cnt++){                // We skip the first 8 bytes of each block beacause they bytes are the id of the block which we dont need
                if(a==4){
                    a=0;
                }
                if(cnt<12){
                    BlockNum[a]=myBufferMeta[cnt];
                }
                else if(cnt<16){
                    Tolatrecords[a]=myBufferMeta[cnt];
                }
                else{
                    RecordsInBlock[a]=myBufferMeta[cnt];
                }
                a++;
            }
            ByteBuffer bfMeta = ByteBuffer.wrap(BlockNum);          //The data from the file are read as byte Sequence so we mast transform the byte sequence to the equivelant number
            IntBuffer lgMeta = bfMeta.asIntBuffer();
            int Blocks_Num = lgMeta.get();
            bfMeta = ByteBuffer.wrap(Tolatrecords);
            lgMeta = bfMeta.asIntBuffer();
            RecordSize = lgMeta.get();
            bfMeta = ByteBuffer.wrap(RecordsInBlock);
            lgMeta = bfMeta.asIntBuffer();
            int Records_Block = lgMeta.get();
            for (int o = 0; o < Blocks_Num; o++) {                                  // We read each block seperatly
                byte[] myBuffer = new byte[32768];
                is.read(myBuffer, 0, 32768);                // Read A 32kb block from the file
                byte ids[][] = new byte[Records_Block][8];                    // Each entry consists of three variable of 8 byte each
                int k = 0;
                int l = 0;
                byte lats[][] = new byte[Records_Block][8];
                byte lots[][] = new byte[Records_Block][8];
                int i = 0;
                int dat_Count = 0;
                for (int m = 8; m < 32768; m++) {                      // We skip the first 8 bytes of each block beacause they bytes are the id of the block which we dont need
                    if (i == 24) {
                        dat_Count++;
                        i = 0;
                        l = 0;
                        k = 0;
                    }
                    if (i < 8) {
                        ids[dat_Count][i] = myBuffer[m];
                    } else if (i < 16) {
                        lats[dat_Count][k] = myBuffer[m];
                        k++;

                    } else {
                        lots[dat_Count][l] = myBuffer[m];
                        l++;
                    }
                    i++;
                }
                //The data from the file are read as byte Sequence so we mast transform the byte sequence to the equivelant number
                for (int c = 0; c < Records_Block; c++) {
                    ByteBuffer bf = ByteBuffer.wrap(ids[c]);
                    LongBuffer lg = bf.asLongBuffer();
                    long tempId = lg.get();
                    bf = ByteBuffer.wrap(lats[c]);
                    DoubleBuffer bs = bf.asDoubleBuffer();
                    Double tempLot = bs.get();
                    bf = ByteBuffer.wrap(lots[c]);
                    DoubleBuffer bl = bf.asDoubleBuffer();
                    double tempLon = bl.get();
                    if (tempId == 0 && tempLot == 0 && tempLon == 0) {
                        break;
                    }
                    locations.add(new Location(tempId, tempLot, tempLon));
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return locations;
    }

    /**
     * In this method we give a specific block id and the register id an it returns the location which is saved in that pair of pointers in the file
     * @param blockId   The id of the block
     * @param registerId The id of the register inside the block
     * @return  The apropriate location
     */
    public Location ReadBlock(int blockId, int registerId) {
        // create a new RandomAccessFile with filename test
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile("dataBlocks.txt", "rw");
            raf.skipBytes(20);
            raf.skipBytes(32768 * --blockId);
            raf.skipBytes(8);
            raf.skipBytes(24*registerId);
            long id= raf.readLong();
            double lat=raf.readDouble();
            double lon=raf.readDouble();
            Location location = new Location(id,lat,lon);
            return location;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
