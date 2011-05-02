package kwic_shared;

public class Index {
	
	private String keyword;
	private int lineIndex;
	private int wordIndex;
	
	public Index(String keyword, int lineIndex, int wordIndex) {
		super();
		this.keyword = keyword;
		this.lineIndex = lineIndex;
		this.wordIndex = wordIndex;
	}
	
	public String getKeyword() {
		return keyword;
	}
	public int getLineIndex() {
		return lineIndex;
	}
	public int getWordIndex() {
		return wordIndex;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}
	public void setWordIndex(int wordIndex) {
		this.wordIndex = wordIndex;
	}
	
	
}
