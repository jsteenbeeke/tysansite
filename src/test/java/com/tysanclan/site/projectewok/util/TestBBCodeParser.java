package com.tysanclan.site.projectewok.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;
import com.tysanclan.site.projectewok.util.bbcode.BBParseException;

public class TestBBCodeParser {
	@Test(timeout = 3000L)
	public void testInvalid() throws BBParseException {
		assertEquals("[Je moeder]", BBCodeUtil.toHtml("[Je moeder]"));
	}

	@Test(expected = BBParseException.class)
	public void testUnclosed() throws BBParseException {
		assertEquals("[b]", BBCodeUtil.toHtml("[b]"));
	}

	@Test
	public void testBold() throws BBParseException {
		assertEquals("<b>Test</b>", BBCodeUtil.toHtml("[b]Test[/b]"));
	}

	@Test
	public void testItalic() throws BBParseException {
		assertEquals("<i>Test</i>", BBCodeUtil.toHtml("[i]Test[/i]"));
	}

	@Test
	public void testUnderline() throws BBParseException {
		assertEquals("<span class=\"underline\">Test</span>",
				BBCodeUtil.toHtml("[u]Test[/u]"));
	}

	@Test
	public void testStrike() throws BBParseException {
		assertEquals("<span class=\"strikethrough\">Test</span>",
				BBCodeUtil.toHtml("[s]Test[/s]"));
	}

	@Test
	public void testQuote() throws BBParseException {
		assertEquals(
				"<blockquote><p><b>Quote</b></p> <p>Test</p></blockquote>",
				BBCodeUtil.toHtml("[quote]Test[/quote]"));
		assertEquals(
				"<blockquote><p><b>Quoting Prospero</b></p> <p>Test</p></blockquote>",
				BBCodeUtil.toHtml("[quote=Prospero]Test[/quote]"));
	}

	@Test
	public void testLink() throws BBParseException {
		assertEquals(
				"<a href=\"http://www.youtube.com/watch?v=a-79sbicwTQ\">http://www.youtube.com/watch?v=a-79sbicwTQ</a>",
				BBCodeUtil
						.toHtml("[url]http://www.youtube.com/watch?v=a-79sbicwTQ[/url]"));
		assertEquals(
				"<a href=\"http://www.youtube.com/watch?v=a-79sbicwTQ\">Swedish Christmas Song</a>",
				BBCodeUtil
						.toHtml("[url=http://www.youtube.com/watch?v=a-79sbicwTQ]Swedish Christmas Song[/url]"));
	}

	@Test
	public void testYoutube() throws BBParseException {
		assertEquals("<div class=\"youtube-replace\">a-79sbicwTQ</div>",
				BBCodeUtil.toHtml("[youtube]a-79sbicwTQ[/youtube]"));
	}

	@Test
	public void testImage() throws BBParseException {
		assertEquals(
				"<img alt=\"http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg\" src=\"http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg\" />",
				BBCodeUtil
						.toHtml("[img]http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg[/img]"));

		assertEquals(
				"<img alt=\"http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg\" style=\"width: 50px;\" src=\"http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg\" />",
				BBCodeUtil
						.toHtml("[img width=50]http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg[/img]"));

		assertEquals(
				"<img alt=\"http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg\" style=\"height: 50px;\" src=\"http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg\" />",
				BBCodeUtil
						.toHtml("[img height=50]http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg[/img]"));

		assertEquals(
				"<img alt=\"http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg\" style=\"width: 50px; height: 50px;\" src=\"http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg\" />",
				BBCodeUtil
						.toHtml("[img height=50 width=50]http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg[/img]"));

		assertEquals(
				"<img alt=\"http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg\" style=\"width: 50px; height: 50px;\" src=\"http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg\" />",
				BBCodeUtil
						.toHtml("[img width=50 height=50]http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg[/img]"));

		assertEquals(
				"<img alt=\"http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg\" style=\"width: 50px; height: 50px;\" src=\"http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg\" />",
				BBCodeUtil
						.toHtml("[img    width=50    height=50]http://www.mentalfloss.com/sites/default/legacy/blogs/wp-content/uploads/2010/02/400pancake_bunny.jpg[/img]"));

	}
}
