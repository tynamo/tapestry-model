package org.trails.component.blob;

public class TrailsBlobImpl implements ITrailsBlob {
    private String fileName;
    private String filePath;
    private String fileExtension;
    private String contentType;
    private byte[] bytes = new byte[0];
    private Long numBytes;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getContentType() {
	    return contentType;
	}

	public void setContentType(String contentType) {
	    this.contentType = contentType;
	}

	public Long getNumBytes() {
		return numBytes;
	}

	public void setNumBytes(Long numBytes) {
		this.numBytes = numBytes;
	}

	public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
