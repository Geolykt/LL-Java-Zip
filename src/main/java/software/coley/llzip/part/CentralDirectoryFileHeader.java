package software.coley.llzip.part;

import software.coley.llzip.ZipCompressions;
import software.coley.llzip.strategy.Decompressor;
import software.coley.llzip.util.Array;

import java.util.Arrays;
import java.util.Objects;

/**
 * ZIP CentralDirectoryFileHeader structure.
 *
 * @author Matt Coley
 */
public class CentralDirectoryFileHeader implements ZipPart, ZipRead {
	private transient int offset = -1;
	private transient LocalFileHeader linkedFileHeader;
	// Zip spec elements
	private int versionMadeBy;
	private int versionNeededToExtract;
	private int generalPurposeBitFlag;
	private int compressionMethod;
	private int lastModFileTime;
	private int lastModFileDate;
	private int crc32;
	private int compressedSize;
	private int uncompressedSize;
	private int fileNameLength;
	private int extraFieldLength;
	private int fileCommentLength;
	private int diskNumberStart;
	private int internalFileAttributes;
	private int externalFileAttributes;
	private int relativeOffsetOfLocalHeader;
	private String fileName;
	private byte[] extraField;
	private String fileComment;

	@Override
	public void read(byte[] data, int offset) {
		this.offset = offset;
		versionMadeBy = Array.readWord(data, offset + 4);
		versionNeededToExtract = Array.readWord(data, offset + 6);
		generalPurposeBitFlag = Array.readWord(data, offset + 8);
		compressionMethod = Array.readWord(data, offset + 10);
		lastModFileTime = Array.readWord(data, offset + 12);
		lastModFileDate = Array.readWord(data, offset + 14);
		crc32 = Array.readQuad(data, offset + 16);
		compressedSize = Array.readQuad(data, offset + 20);
		uncompressedSize = Array.readQuad(data, offset + 24);
		fileNameLength = Array.readWord(data, offset + 28);
		extraFieldLength = Array.readWord(data, offset + 30);
		fileCommentLength = Array.readWord(data, offset + 32);
		diskNumberStart = Array.readWord(data, offset + 34);
		internalFileAttributes = Array.readWord(data, offset + 36);
		externalFileAttributes = Array.readQuad(data, offset + 38);
		relativeOffsetOfLocalHeader = Array.readQuad(data, offset + 42);
		fileName = Array.readString(data, offset + 46, fileNameLength);
		extraField = Array.readArray(data, offset + 46 + fileNameLength, extraFieldLength);
		fileComment = Array.readString(data, offset + 46 + fileNameLength + extraFieldLength, fileCommentLength);
	}

	@Override
	public int length() {
		return 46 + fileName.length() + extraField.length + fileComment.length();
	}

	@Override
	public PartType type() {
		return PartType.CENTRAL_DIRECTORY_FILE_HEADER;
	}

	@Override
	public int offset() {
		return offset;
	}

	/**
	 * @return The file header associated with {@link #getRelativeOffsetOfLocalHeader()}. May be {@code null}.
	 */
	public LocalFileHeader getLinkedFileHeader() {
		return linkedFileHeader;
	}

	/**
	 * @param header
	 * 		The file header associated with {@link #getRelativeOffsetOfLocalHeader()}. May be {@code null}.
	 */
	public void link(LocalFileHeader header) {
		this.linkedFileHeader = header;
	}

	/**
	 * @return Version of zip software used to make the archive.
	 */
	public int getVersionMadeBy() {
		return versionMadeBy;
	}

	/**
	 * @param versionMadeBy
	 * 		Version of zip software used to make the archive.
	 */
	public void setVersionMadeBy(int versionMadeBy) {
		this.versionMadeBy = versionMadeBy;
	}

	/**
	 * @return Version of zip software required to read the archive features.
	 */
	public int getVersionNeededToExtract() {
		return versionNeededToExtract;
	}

	/**
	 * @param versionNeededToExtract
	 * 		Version of zip software required to read the archive features.
	 */
	public void setVersionNeededToExtract(int versionNeededToExtract) {
		this.versionNeededToExtract = versionNeededToExtract;
	}

	/**
	 * @return Used primarily to expand on details of file compression.
	 */
	public int getGeneralPurposeBitFlag() {
		return generalPurposeBitFlag;
	}

	/**
	 * @param generalPurposeBitFlag
	 * 		Used primarily to expand on details of file compression.
	 */
	public void setGeneralPurposeBitFlag(int generalPurposeBitFlag) {
		this.generalPurposeBitFlag = generalPurposeBitFlag;
	}

	/**
	 * @return Method to use for {@link LocalFileHeader#decompress(Decompressor) decompressing data}.
	 *
	 * @see ZipCompressions Possible methods.
	 */
	public int getCompressionMethod() {
		return compressionMethod;
	}

	/**
	 * @param compressionMethod
	 * 		Method to use for {@link LocalFileHeader#decompress(Decompressor) decompressing data}.
	 *
	 * @see ZipCompressions Possible methods.
	 */
	public void setCompressionMethod(int compressionMethod) {
		this.compressionMethod = compressionMethod;
	}

	/**
	 * @return Modification time of the file.
	 */
	public int getLastModFileTime() {
		return lastModFileTime;
	}

	/**
	 * @param lastModFileTime
	 * 		Modification time of the file.
	 */
	public void setLastModFileTime(int lastModFileTime) {
		this.lastModFileTime = lastModFileTime;
	}

	/**
	 * @return Modification date of the file.
	 */
	public int getLastModFileDate() {
		return lastModFileDate;
	}

	/**
	 * @param lastModFileDate
	 * 		Modification date of the file.
	 */
	public void setLastModFileDate(int lastModFileDate) {
		this.lastModFileDate = lastModFileDate;
	}

	/**
	 * @return File checksum.
	 */
	public int getCrc32() {
		return crc32;
	}

	/**
	 * @param crc32
	 * 		File checksum.
	 */
	public void setCrc32(int crc32) {
		this.crc32 = crc32;
	}

	/**
	 * Be aware that these attributes can be falsified.
	 * Different zip-parsing programs treat the files differently
	 * and may not adhere to what you expect from the zip specification.
	 * <br>
	 * When in doubt, trust {@link byte[]#length()} from {@link LocalFileHeader#getFileData()}.
	 *
	 * @return Compressed size of {@link LocalFileHeader#getFileData()}.
	 */
	public int getCompressedSize() {
		return compressedSize;
	}

	/**
	 * @param compressedSize
	 * 		Compressed size of {@link LocalFileHeader#getFileData()}.
	 */
	public void setCompressedSize(int compressedSize) {
		this.compressedSize = compressedSize;
	}

	/**
	 * Be aware that these attributes can be falsified.
	 * Different zip-parsing programs treat the files differently
	 * and may not adhere to what you expect from the zip specification.
	 *
	 * @return Uncompressed size after {@link LocalFileHeader#decompress(Decompressor)} is used on {@link LocalFileHeader#getFileData()}.
	 */
	public int getUncompressedSize() {
		return uncompressedSize;
	}

	/**
	 * @param uncompressedSize
	 * 		Uncompressed size after {@link LocalFileHeader#decompress(Decompressor)} is used on {@link LocalFileHeader#getFileData()}.
	 */
	public void setUncompressedSize(int uncompressedSize) {
		this.uncompressedSize = uncompressedSize;
	}

	/**
	 * @return Length of {@link #getFileName()}.
	 */
	public int getFileNameLength() {
		return fileNameLength;
	}

	/**
	 * @param fileNameLength
	 * 		Length of {@link #getFileName()}.
	 */
	public void setFileNameLength(int fileNameLength) {
		this.fileNameLength = fileNameLength;
	}

	/**
	 * @return Length of {@link #getExtraField()}
	 */
	public int getExtraFieldLength() {
		return extraFieldLength;
	}

	/**
	 * @param extraFieldLength
	 * 		Length of {@link #getExtraField()}
	 */
	public void setExtraFieldLength(int extraFieldLength) {
		this.extraFieldLength = extraFieldLength;
	}

	/**
	 * @return Length of {@link #getFileComment()}.
	 */
	public int getFileCommentLength() {
		return fileCommentLength;
	}

	/**
	 * @param fileCommentLength
	 * 		Length of {@link #getFileComment()}.
	 */
	public void setFileCommentLength(int fileCommentLength) {
		this.fileCommentLength = fileCommentLength;
	}

	/**
	 * @return Disk number where the archive starts from, or {@link 0xFFFF} for ZIP64.
	 */
	public int getDiskNumberStart() {
		return diskNumberStart;
	}

	/**
	 * @param diskNumberStart
	 * 		Disk number where the archive starts from, or {@link 0xFFFF} for ZIP64.
	 */
	public void setDiskNumberStart(int diskNumberStart) {
		this.diskNumberStart = diskNumberStart;
	}

	/**
	 * The lowest bit of this field indicates, if set,
	 * that the file is apparently an ASCII or text file.
	 * If not set, that the file apparently contains binary data.
	 * <br>
	 * The {@code 0x0002} bit of this field indicates, if set,
	 * that a 4 byte variable record length control field precedes each
	 * logical record indicating the length of the record.
	 *
	 * @return Internal attributes used for inferring content type.
	 */
	public int getInternalFileAttributes() {
		return internalFileAttributes;
	}

	/**
	 * @param internalFileAttributes
	 * 		Internal attributes used for inferring content type.
	 */
	public void setInternalFileAttributes(int internalFileAttributes) {
		this.internalFileAttributes = internalFileAttributes;
	}

	/**
	 * For MS-DOS, the low order byte is the MS-DOS directory attribute byte.
	 * If input came from standard input, this field is zero.
	 *
	 * @return Host system dependent attributes.
	 */
	public int getExternalFileAttributes() {
		return externalFileAttributes;
	}

	/**
	 * @param externalFileAttributes
	 * 		Host system dependent attributes.
	 */
	public void setExternalFileAttributes(int externalFileAttributes) {
		this.externalFileAttributes = externalFileAttributes;
	}

	/**
	 * @return Offset from the start of the {@link #getDiskNumberStart() first disk} where the file appears.
	 * This should also be where the {@link LocalFileHeader} is located.  Or {@code 0xFFFFFFFF} for ZIP64.
	 */
	public int getRelativeOffsetOfLocalHeader() {
		return relativeOffsetOfLocalHeader;
	}

	/**
	 * @param relativeOffsetOfLocalHeader
	 * 		Offset from the start of the {@link #getDiskNumberStart() first disk} where the file appears.
	 * 		This should also be where the {@link LocalFileHeader} is located.  Or {@code 0xFFFFFFFF} for ZIP64.
	 */
	public void setRelativeOffsetOfLocalHeader(int relativeOffsetOfLocalHeader) {
		this.relativeOffsetOfLocalHeader = relativeOffsetOfLocalHeader;
	}

	/**
	 * Should match {@link LocalFileHeader#getFileName()} but is not a strict requirement.
	 * If they do not match, trust this value instead.
	 *
	 * @return File name.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 * 		File name.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return May be used for extra compression information,
	 * depending on the {@link #getCompressionMethod() compression method} used.
	 */
	public byte[] getExtraField() {
		return extraField;
	}

	/**
	 * @param extraField
	 * 		Extra field bytes.
	 */
	public void setExtraField(byte[] extraField) {
		this.extraField = extraField;
	}

	/**
	 * @return File comment.
	 */
	public String getFileComment() {
		return fileComment;
	}

	/**
	 * @param fileComment
	 * 		File comment.
	 */
	public void setFileComment(String fileComment) {
		this.fileComment = fileComment;
	}

	@Override
	public String toString() {
		return "CentralDirectoryFileHeader{" +
				"  versionMadeBy=" + versionMadeBy +
				", versionNeededToExtract=" + versionNeededToExtract +
				", generalPurposeBitFlag=" + generalPurposeBitFlag +
				", compressionMethod=" + compressionMethod +
				", lastModFileTime=" + lastModFileTime +
				", lastModFileDate=" + lastModFileDate +
				", crc32=" + crc32 +
				", compressedSize=" + compressedSize +
				", uncompressedSize=" + uncompressedSize +
				", fileNameLength=" + fileNameLength +
				", extraFieldLength=" + extraFieldLength +
				", fileCommentLength=" + fileCommentLength +
				", diskNumberStart=" + diskNumberStart +
				", internalFileAttributes=" + internalFileAttributes +
				", externalFileAttributes=" + externalFileAttributes +
				", relativeOffsetOfLocalHeader=" + relativeOffsetOfLocalHeader +
				", fileName='" + fileName + '\'' +
				", extraField=" + Arrays.toString(extraField) +
				", fileComment='" + fileComment + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CentralDirectoryFileHeader that = (CentralDirectoryFileHeader) o;
		return offset == that.offset &&
				versionMadeBy == that.versionMadeBy &&
				versionNeededToExtract == that.versionNeededToExtract &&
				generalPurposeBitFlag == that.generalPurposeBitFlag &&
				compressionMethod == that.compressionMethod &&
				lastModFileTime == that.lastModFileTime &&
				lastModFileDate == that.lastModFileDate &&
				crc32 == that.crc32 &&
				compressedSize == that.compressedSize &&
				uncompressedSize == that.uncompressedSize &&
				fileNameLength == that.fileNameLength &&
				extraFieldLength == that.extraFieldLength &&
				fileCommentLength == that.fileCommentLength &&
				diskNumberStart == that.diskNumberStart &&
				internalFileAttributes == that.internalFileAttributes &&
				externalFileAttributes == that.externalFileAttributes &&
				relativeOffsetOfLocalHeader == that.relativeOffsetOfLocalHeader &&
				Objects.equals(linkedFileHeader, that.linkedFileHeader) &&
				fileName.equals(that.fileName) &&
				Arrays.equals(extraField, that.extraField) &&
				fileComment.equals(that.fileComment);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(offset, linkedFileHeader, versionMadeBy, versionNeededToExtract, generalPurposeBitFlag,
				compressionMethod, lastModFileTime, lastModFileDate, crc32, compressedSize, uncompressedSize,
				fileNameLength, extraFieldLength, fileCommentLength, diskNumberStart, internalFileAttributes,
				externalFileAttributes, relativeOffsetOfLocalHeader, fileName, fileComment);
		result = 31 * result + Arrays.hashCode(extraField);
		return result;
	}
}
