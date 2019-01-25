package common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import javafx.scene.image.Image;

/**
 * Class to contain big files to send.
 * 
 * @author Roma
 *
 */
public class SendableFile implements Serializable {

	private static final long serialVersionUID = -5726470308041960871L;
	private String mFullFilePath = null;
	private String mFileName = null;	// image.jpg
	private int mSize = 0;
	private byte[] mByteArray;
	
	
	/**
	 * Function to initialize SendableFile parameters
	 * @param file to build SendableFile from.
	 */
	private void initSendableFile(File file) {
		try {
			  mFullFilePath = file.getPath();

			  mFileName = mFullFilePath.substring(mFullFilePath.lastIndexOf(File.separator)+1);
			  mSize = (int) file.length();
			  mByteArray  = new byte[mSize];
			  BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));			  
			  bis.read(mByteArray, 0, mSize);
			  bis.close();
			}
			catch(Exception e) {
				System.err.println(e.toString());
				mByteArray = null;
				mSize = 0;
			}
	
	}
	

	public SendableFile(String fullFilePath) {
		File file = new File(fullFilePath);
		initSendableFile(file);
	}
	
	public SendableFile(File file) {
		initSendableFile(file);
	}
	
	/**
	 * @return the file's filename + extenstion.
	 */
	public String getFileName() {
		return mFileName;
	}

	/**
	 * @return file size
	 */
	public int getSize() {
		return mSize;
	}

	/**
	 * @return	the absolute file path.
	 */
	public String getFullFilePath() {
		return mFullFilePath;
	}
	
	/**
	 * Save received file to filePath (to Disk).
	 * 
	 * @param folderPath the folder's path.
	 * @throws IOException exception
	 */
	public void commitFile(String folderPath) throws IOException {
		String filePath = folderPath + mFileName;
		System.out.println(filePath);
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
		out.write(mByteArray);
		mFullFilePath = filePath;
		out.close();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SendableFile [mFullFilePath=" + mFullFilePath + ", mFileName=" + mFileName + ", mSize=" + mSize
				+ ", mByteArray=" + Arrays.toString(mByteArray) + "]";
	}

	public void setFullFilePath(String fullFilePath) {
		mFullFilePath = fullFilePath;
	}
	
	/**
	 * Get the File as Image
	 * @return	Image file
	 */
	public Image getImage() {
		if (mByteArray == null)
			return null;
		else
			return new Image(new ByteArrayInputStream(mByteArray));
	}
	
}

