public class VikipediParser {
    public String[] parse(String content) {

	int idTagStart = content.indexOf("<DOCNO>");
	int idTagEnd = content.indexOf("</DOCNO>");
	int titleTagStart = content.indexOf("<DOCTITLE>");
	int titleTagEnd = content.indexOf("</DOCTITLE>");
	int textTagStart = content.indexOf("<TEXT>");
	int textTagEnd = content.indexOf("</TEXT>");

	return new String[] { content.substring(idTagStart + 7, idTagEnd),
			      content.substring(titleTagStart + 10, titleTagEnd),
			      content.substring(textTagStart + 6, textTagEnd) };
    }
}
