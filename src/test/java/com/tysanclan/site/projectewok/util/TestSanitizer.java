/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jeroen Steenbeeke
 */
public class TestSanitizer {
	private static final String[][] INPUT_OUTPUT_PAIRS = {
			// Test 1
			{ "<img src=\"test.png\" alt=\"Test\" />",
					"<img src=\"test.png\" alt=\"Test\" />" },
			// Test 2
			{ "<IMG SRC=\"javascript:alert('XSS');\">", "" },
			// Test 3
			{ "<IMG SRC=javascript:alert('XSS')>", "" },
			// Test 4
			{ "<IMG SRC=JaVaScRiPt:alert('XSS')>", "" },
			// Test 5
			{ "<IMG SRC=javascript:alert(&quot;XSS&quot;)>", "" },
			// Test 6
			{ "<IMG SRC=`javascript:alert(\"RSnake says, 'XSS'\")`>", "" },
			// Test 7
			{ "<IMG \"\"\"><SCRIPT>alert(\"XSS\")</SCRIPT>\">",
					"&lt;SCRIPT&gt;alert(&quot;XSS&quot;)&lt;/SCRIPT&gt;&quot;&gt;" },
			// Test 8
			{ "<IMG SRC=javascript:alert(String.fromCharCode(88,83,83))>", "" },
			// Test 9
			{
					"<IMG SRC=&#106;&#97;&#118;&#97;&#115;&#99;&#114;&#105;&#112;&#116;&#58;&#97;&#108;&#101;&#114;&#116;&#40;&#39;&#88;&#83;&#83;&#39;&#41;>",
					"" },
			// Test 10
			{
					"<IMG SRC=&#0000106&#0000097&#0000118&#0000097&#0000115&#0000099&#0000114&#0000105&#0000112&#0000116&#0000058&#0000097&#0000108&#0000101&#0000114&#0000116&#0000040&#0000039&#0000088&#0000083&#0000083&#0000039&#0000041>",
					"" },
			// Test 11
			{
					"<IMG SRC=&#x6A&#x61&#x76&#x61&#x73&#x63&#x72&#x69&#x70&#x74&#x3A&#x61&#x6C&#x65&#x72&#x74&#x28&#x27&#x58&#x53&#x53&#x27&#x29>",
					"" },
			// Test 12
			{ "<IMG SRC=\"jav	ascript:alert('XSS');\">", "" },
			// Test 13
			{ "<IMG SRC=\"jav&#x09;ascript:alert('XSS');\">", "" },
			// Test 14
			{ "<IMG SRC=\"jav&#x0A;ascript:alert('XSS');\">", "" },
			// Test 15
			{ "<IMG SRC=\"jav&#x0D;ascript:alert('XSS');\">", "" },
			// Test 16
			{
					"<IMG\u240DSRC\u240D=\u240D\"\u240Dj\u240Da\u240Dv\u240Da\u240Ds\u240Dc\u240Dr\u240Di\u240Dp\u240Dt\u240D:\u240Da\u240Dl\u240De\u240Dr\u240Dt\u240D(\u240D'\u240DX\u240DS\u240DS\u240D'\u240D)\u240D\"\u240D>",
					"&lt;IMG\u240DSRC\u240D=\u240D&quot;\u240Dj\u240Da\u240Dv\u240Da\u240Ds\u240Dc\u240Dr\u240Di\u240Dp\u240Dt\u240D:\u240Da\u240Dl\u240De\u240Dr\u240Dt\u240D(\u240D'\u240DX\u240DS\u240DS\u240D'\u240D)\u240D&quot;\u240D&gt;" },
			// Test 17
			{ "<IMG SRC=java\0script:alert(\"XSS\")>", "" },
			// Test 18
			{ "<SCR\0IPT>alert(\\\"XSS\\\")</SCR\0IPT>",
					"&lt;SCR\0IPT&gt;alert(\\&quot;XSS\\&quot;)&lt;/SCR\0IPT&gt;" },
			// Test 19
			{ "<IMG SRC=\" &#14;  javascript:alert('XSS');\">", "" },
			// Test 20
			{ "<SCRIPT/XSS SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>",
					"&lt;SCRIPT/XSS SRC=&quot;http://ha.ckers.org/xss.js&quot;&gt;&lt;/SCRIPT&gt;" },
			// Test 21
			{ "<BODY onload!#$%&()*~+-_.,:;?@[/|\\]^`=alert(\"XSS\")>",
					"&lt;BODY onload!#$%&()*~+-_.,:;?@[/|\\]^`=alert(&quot;XSS&quot;)&gt;" },
			// Test 22
			{ "<SCRIPT/SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>",
					"&lt;SCRIPT/SRC=&quot;http://ha.ckers.org/xss.js&quot;&gt;&lt;/SCRIPT&gt;" },
			// Test 23
			{ "<<SCRIPT>alert(\"XSS\");//<</SCRIPT>",
					"&lt;&lt;SCRIPT&gt;alert(&quot;XSS&quot;);//&lt;&lt;/SCRIPT&gt;" },
			// Test 24
			{ "<SCRIPT SRC=http://ha.ckers.org/xss.js?<B>",
					"&lt;SCRIPT SRC=http://ha.ckers.org/xss.js?&lt;B&gt;" },
			// Test 25
			{ "<SCRIPT SRC=//ha.ckers.org/.j>",
					"&lt;SCRIPT SRC=//ha.ckers.org/.j&gt;" },
			// Test 26
			{ "<IMG SRC=\"javascript:alert('XSS')\"", "" },
			// Test 27
			{ "<iframe src=http://ha.ckers.org/scriptlet.html <",
					"&lt;iframe src=http://ha.ckers.org/scriptlet.html &lt;" },
			// Test 28
			{ "<SCRIPT>a=/XSS/alert(a.source)</SCRIPT>",
					"&lt;SCRIPT&gt;a=/XSS/alert(a.source)&lt;/SCRIPT&gt;" },
			// Test 29
			{ "<IMG DYNSRC=\"javascript:alert('XSS')\">", "" },
			// Test 30
			{ "<BODY ONLOAD=alert('XSS')>", "&lt;BODY ONLOAD=alert('XSS')&gt;" },
			// Test 31
			{ "</TITLE><SCRIPT>alert(\"XSS\");</SCRIPT>",
					"&lt;/TITLE&gt;&lt;SCRIPT&gt;alert(&quot;XSS&quot;);&lt;/SCRIPT&gt;" },
			// Test 32
			{ "<INPUT TYPE=\"IMAGE\" SRC=\"javascript:alert('XSS');\">",
					"&lt;INPUT TYPE=&quot;IMAGE&quot; SRC=&quot;javascript:alert('XSS');&quot;&gt;" },
			// Test 33
			{ "<BODY BACKGROUND=\"javascript:alert('XSS')\">",
					"&lt;BODY BACKGROUND=&quot;javascript:alert('XSS')&quot;&gt;" },
			// Test 34
			{ "<IMG LOWSRC=\"javascript:alert('XSS')\">", "" },
			// Test 35
			{ "<BR SIZE=\"&{alert('XSS')}\">", "<br />" },
			// Test 36
			{
					"<LAYER SRC=\"http://ha.ckers.org/scriptlet.html\"></LAYER>",
					"&lt;LAYER SRC=&quot;http://ha.ckers.org/scriptlet.html&quot;&gt;&lt;/LAYER&gt;" },
			// Test 37
			{
					"<LINK REL=\"stylesheet\" HREF=\"javascript:alert('XSS');\">",
					"&lt;LINK REL=&quot;stylesheet&quot; HREF=&quot;javascript:alert('XSS');&quot;&gt;" },
			// Test 38
			{
					"<LINK REL=\"stylesheet\" HREF=\"http://ha.ckers.org/xss.css\">",
					"&lt;LINK REL=&quot;stylesheet&quot; HREF=&quot;http://ha.ckers.org/xss.css&quot;&gt;" },
			// Test 39
			{ "<STYLE>@import'http://ha.ckers.org/xss.css';</STYLE>",
					"&lt;STYLE&gt;@import'http://ha.ckers.org/xss.css';&lt;/STYLE&gt;" },
			// Test 40
			{
					"<META HTTP-EQUIV=\"Link\" Content=\"<http://ha.ckers.org/xss.css>; REL=stylesheet\">",
					"&lt;META HTTP-EQUIV=&quot;Link&quot; Content=&quot;&lt;http://ha.ckers.org/xss.css&gt;; REL=stylesheet&quot;&gt;" },
			// Test 41
			{
					"<STYLE>BODY{-moz-binding:url(\"http://ha.ckers.org/xssmoz.xml#xss\")}</STYLE>",
					"&lt;STYLE&gt;BODY{-moz-binding:url(&quot;http://ha.ckers.org/xssmoz.xml#xss&quot;)}&lt;/STYLE&gt;" },
			// Test 42
			{ "<XSS STYLE=\"behavior: url(xss.htc);\">",
					"&lt;XSS STYLE=&quot;behavior: url(xss.htc);&quot;&gt;" },
			// Test 43
			{ "<BGSOUND SRC=\"javascript:alert('XSS');\">",
					"&lt;BGSOUND SRC=&quot;javascript:alert('XSS');&quot;&gt;" },
			// Test 44
			{
					"<STYLE>li {list-style-image: url(\"javascript:alert('XSS')\");}</STYLE><UL><LI>XSS",
					"&lt;STYLE&gt;li {list-style-image: url(&quot;javascript:alert('XSS')&quot;);}&lt;/STYLE&gt;<ul><li>XSS" },
			// Test 45
			{ "<IMG SRC='vbscript:msgbox(\"XSS\")'>", "" },
			// Test 46
			{ "<IMG SRC=\"mocha:[code]\">", "" },
			// Test 47
			{ "<IMG SRC=\"livescript:[code]\">", "" },
			// Test 48
			{ "\u00BCscript\u00BEalert(\u00A2XSS\u00A2)\u00BC/script\u00BE",
					"scriptalert(\u00A2XSS\u00A2)/script" },
			// Test 49
			{
					"<META HTTP-EQUIV=\"refresh\" CONTENT=\"0;url=javascript:alert('XSS');\">",
					"&lt;META HTTP-EQUIV=&quot;refresh&quot; CONTENT=&quot;0;url=javascript:alert('XSS');&quot;&gt;" },
			// Test 50
			{
					"<META HTTP-EQUIV=\"refresh\" CONTENT=\"0;url=data:text/html;base64,PHNjcmlwdD5hbGVydCgnWFNTJyk8L3NjcmlwdD4K\">",
					"&lt;META HTTP-EQUIV=&quot;refresh&quot; CONTENT=&quot;0;url=data:text/html;base64,PHNjcmlwdD5hbGVydCgnWFNTJyk8L3NjcmlwdD4K&quot;&gt;" },
			// Test 51
			{
					"<META HTTP-EQUIV=\"refresh\" CONTENT=\"0; URL=http://;URL=javascript:alert('XSS');\">",
					"&lt;META HTTP-EQUIV=&quot;refresh&quot; CONTENT=&quot;0; URL=http://;URL=javascript:alert('XSS');&quot;&gt;" },
			// Test 52
			{ "<IFRAME SRC=\"javascript:alert('XSS');\"></IFRAME>",
					"&lt;IFRAME SRC=&quot;javascript:alert('XSS');&quot;&gt;&lt;/IFRAME&gt;" },
			// Test 53
			{
					"<FRAMESET><FRAME SRC=\"javascript:alert('XSS');\"></FRAMESET>",
					"&lt;FRAMESET&gt;&lt;FRAME SRC=&quot;javascript:alert('XSS');&quot;&gt;&lt;/FRAMESET&gt;" },
			// Test 54
			{ "<TABLE BACKGROUND=\"javascript:alert('XSS')\">",
					"&lt;TABLE BACKGROUND=&quot;javascript:alert('XSS')&quot;&gt;" },
			// Test 55
			{ "<TABLE><TD BACKGROUND=\"javascript:alert('XSS')\">",
					"&lt;TABLE&gt;&lt;TD BACKGROUND=&quot;javascript:alert('XSS')&quot;&gt;" },
			// Test 56
			{ "<DIV STYLE=\"background-image: url(javascript:alert('XSS'))\">",
					"<div>STYLE=&quot;background-image: url(javascript:alert('XSS'))&quot;&gt;" },
			// Test 57
			{
					"<DIV STYLE=\"background-image:\0075\0072\006C\0028'\006a\0061\0076\0061\0073\0063\0072\0069\0070\0074\003a\0061\006c\0065\0072\0074\0028.1027\0058.1053\0053\0027\0029'\0029\">",
					"<div>STYLE=&quot;background-image:\0075\0072\006C\0028'\006a\0061\0076\0061\0073\0063\0072\0069\0070\0074\003a\0061\006c\0065\0072\0074\0028.1027\0058.1053\0053\0027\0029'\0029&quot;&gt;" },
			// Test 58
			{
					"<DIV STYLE=\"background-image: url(&#1;javascript:alert('XSS'))\">",
					"<div>STYLE=&quot;background-image: url(&#1;javascript:alert('XSS'))&quot;&gt;" },
			// Test 59
			{ "<DIV STYLE=\"width: expression(alert('XSS'));\">",
					"<div>STYLE=&quot;width: expression(alert('XSS'));&quot;&gt;" },
			// Test 60
			{
					"<STYLE>@im\\port'\\ja\\vasc\\ript:alert(\"XSS\")';</STYLE>",
					"&lt;STYLE&gt;@im\\port'\\ja\\vasc\\ript:alert(&quot;XSS&quot;)';&lt;/STYLE&gt;" },
			// Test 61
			{ "<IMG STYLE=\"xss:expr/*XSS*/ession(alert('XSS'))\">", "" },
			// Test 62
			{ "<XSS STYLE=\"xss:expression(alert('XSS'))\">",
					"&lt;XSS STYLE=&quot;xss:expression(alert('XSS'))&quot;&gt;" },
			// Test 63
			{
					"exp/*<A STYLE='no\\xss:noxss(\"*//*\");xss:&#101;x&#x2F;*XSS*//*/*/pression(alert(\"XSS\"))'>",
					"exp/*<a href=\"\">" },
			// Test 64
			{ "<STYLE TYPE=\"text/javascript\">alert('XSS');</STYLE>",
					"&lt;STYLE TYPE=&quot;text/javascript&quot;&gt;alert('XSS');&lt;/STYLE&gt;" },
			// Test 65
			{
					"<STYLE>.XSS{background-image:url(\"javascript:alert('XSS')\");}</STYLE><A CLASS=XSS></A>",
					"&lt;STYLE&gt;.XSS{background-image:url(&quot;javascript:alert('XSS')&quot;);}&lt;/STYLE&gt;<a href=\"\"></a>" },
			// Test 66
			{
					"<STYLE type=\"text/css\">BODY{background:url(\"javascript:alert('XSS')\")}</STYLE>",
					"&lt;STYLE type=&quot;text/css&quot;&gt;BODY{background:url(&quot;javascript:alert('XSS')&quot;)}&lt;/STYLE&gt;" },
			// Test 67
			{
					"<!--[if gte IE 4]>\n<SCRIPT>alert('XSS');</SCRIPT>\n<![endif]-->",
					"&lt;!--[if gte IE 4]&gt;\n&lt;SCRIPT&gt;alert('XSS');&lt;/SCRIPT&gt;\n&lt;![endif]--&gt;" },
			// Test 68
			{ "<BASE HREF=\"javascript:alert('XSS');//\">",
					"&lt;BASE HREF=&quot;javascript:alert('XSS');//&quot;&gt;" },
			// Test 69 REMOVED
			// Test 70
			{
					"<OBJECT classid=clsid:ae24fdae-03c6-11d1-8b76-0080c744f389><param name=url value=javascript:alert('XSS')></OBJECT>",
					" classid=clsid:ae24fdae-03c6-11d1-8b76-0080c744f389&gt; name=url value=javascript:alert('XSS')&gt;</object>" },
			// Test 71
			{
					"<EMBED SRC=\"http://ha.ckers.org/xss.swf\" AllowScriptAccess=\"always\"></EMBED>",
					" SRC=&quot;http://ha.ckers.org/xss.swf&quot; AllowScriptAccess=&quot;always&quot;&gt;</embed>" },
			// Test 72
			{
					"<EMBED SRC=\"data:image/svg+xml;base64,PHN2ZyB4bWxuczpzdmc9Imh0dH A6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcv MjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hs aW5rIiB2ZXJzaW9uPSIxLjAiIHg9IjAiIHk9IjAiIHdpZHRoPSIxOTQiIGhlaWdodD0iMjAw IiBpZD0ieHNzIj48c2NyaXB0IHR5cGU9InRleHQvZWNtYXNjcmlwdCI+YWxlcnQoIlh TUyIpOzwvc2NyaXB0Pjwvc3ZnPg==\" type=\"image/svg+xml\" AllowScriptAccess=\"always\"></EMBED>",
					" SRC=&quot;data:image/svg+xml;base64,PHN2ZyB4bWxuczpzdmc9Imh0dH A6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcv MjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hs aW5rIiB2ZXJzaW9uPSIxLjAiIHg9IjAiIHk9IjAiIHdpZHRoPSIxOTQiIGhlaWdodD0iMjAw IiBpZD0ieHNzIj48c2NyaXB0IHR5cGU9InRleHQvZWNtYXNjcmlwdCI+YWxlcnQoIlh TUyIpOzwvc2NyaXB0Pjwvc3ZnPg==&quot; type=&quot;image/svg+xml&quot; AllowScriptAccess=&quot;always&quot;&gt;</embed>" },
			// Test 73
			{
					"<HTML xmlns:xss><?import namespace=\"xss\" implementation=\"http://ha.ckers.org/xss.htc\"><xss:xss>XSS</xss:xss></HTML>",
					"&lt;HTML xmlns:xss&gt;&lt;?import namespace=&quot;xss&quot; implementation=&quot;http://ha.ckers.org/xss.htc&quot;&gt;&lt;xss:xss&gt;XSS&lt;/xss:xss&gt;&lt;/HTML&gt;" },
			// Test 74
			{
					"<XML ID=I><X><C><![CDATA[<IMG SRC=\"javas]]><![CDATA[cript:alert('XSS');\">]]></C></X></xml><SPAN DATASRC=#I DATAFLD=C DATAFORMATAS=HTML></SPAN>",
					"&lt;XML ID=I&gt;&lt;X&gt;&lt;C&gt;&lt;![CDATA[&lt;![CDATA[cript:alert('XSS');&quot;&gt;]]&gt;&lt;/C&gt;&lt;/X&gt;&lt;/xml&gt;<span></span>" },
			// Test 75
			{
					"<XML ID=\"xss\"><I><B>&lt;IMG SRC=\"javas<!-- -->cript:alert('XSS')\"&gt;</B></I></XML><SPAN DATASRC=\"#xss\" DATAFLD=\"B\" DATAFORMATAS=\"HTML\"></SPAN>",
					"&lt;XML ID=&quot;xss&quot;&gt;&lt;I&gt;&lt;B&gt;&lt;IMG SRC=&quot;javas&lt;!-- --&gt;cript:alert('XSS')&quot;&gt;&lt;/B&gt;&lt;/I&gt;&lt;/XML&gt;<span></span>" },
			// Test 76
			{
					"<XML SRC=\"xsstest.xml\" ID=I></XML><SPAN DATASRC=#I DATAFLD=C DATAFORMATAS=HTML></SPAN>",
					"&lt;XML SRC=&quot;xsstest.xml&quot; ID=I&gt;&lt;/XML&gt;<span></span>" },
			// Test 77
			{
					"<HTML><BODY><?xml:namespace prefix=\"t\" ns=\"urn:schemas-microsoft-com:time\"><?import namespace=\"t\" implementation=\"#default#time2\"><t:set attributeName=\"innerHTML\" to=\"XSS&lt;SCRIPT DEFER&gt;alert(&quot;XSS&quot;)&lt;/SCRIPT&gt;\"></BODY></HTML>",
					"&lt;HTML&gt;&lt;BODY&gt;&lt;?xml:namespace prefix=&quot;t&quot; ns=&quot;urn:schemas-microsoft-com:time&quot;&gt;&lt;?import namespace=&quot;t&quot; implementation=&quot;#default#time2&quot;&gt;&lt;t:set attributeName=&quot;innerHTML&quot; to=&quot;XSS&lt;SCRIPT DEFER&gt;alert(&quot;XSS&quot;)&lt;/SCRIPT&gt;&quot;&gt;&lt;/BODY&gt;&lt;/HTML&gt;" },
			// Test 78
			{ "<SCRIPT SRC=\"http://ha.ckers.org/xss.jpg\"></SCRIPT>",
					"&lt;SCRIPT SRC=&quot;http://ha.ckers.org/xss.jpg&quot;&gt;&lt;/SCRIPT&gt;" },
			// Test 79
			{
					"<!--#exec cmd=\"/bin/echo '<SCR'\"--><!--#exec cmd=\"/bin/echo 'IPT SRC=http://ha.ckers.org/xss.js></SCRIPT>'\"-->",
					"&lt;!--#exec cmd=&quot;/bin/echo '&lt;SCR'&quot;--&gt;&lt;!--#exec cmd=&quot;/bin/echo 'IPT SRC=http://ha.ckers.org/xss.js&gt;&lt;/SCRIPT&gt;'&quot;--&gt;" },
			// Test 80
			{
					"<META HTTP-EQUIV=\"Set-Cookie\" Content=\"USERID=&lt;SCRIPT&gt;alert('XSS')&lt;/SCRIPT&gt;\">",
					"&lt;META HTTP-EQUIV=&quot;Set-Cookie&quot; Content=&quot;USERID=&lt;SCRIPT&gt;alert('XSS')&lt;/SCRIPT&gt;&quot;&gt;" },
			// Test 81
			{
					"<HEAD><META HTTP-EQUIV=\"CONTENT-TYPE\" CONTENT=\"text/html; charset=UTF-7\"> </HEAD>+ADw-SCRIPT+AD4-alert('XSS');+ADw-/SCRIPT+AD4-",
					"&lt;HEAD&gt;&lt;META HTTP-EQUIV=&quot;CONTENT-TYPE&quot; CONTENT=&quot;text/html; charset=UTF-7&quot;&gt; &lt;/HEAD&gt;+ADw-SCRIPT+AD4-alert('XSS');+ADw-/SCRIPT+AD4-" },
			// Test 82
			{
					"<SCRIPT a=\">\" SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>",
					"&lt;SCRIPT a=&quot;&gt;&quot; SRC=&quot;http://ha.ckers.org/xss.js&quot;&gt;&lt;/SCRIPT&gt;" },
			// Test 83
			{
					"<SCRIPT =\">\" SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>",
					"&lt;SCRIPT =&quot;&gt;&quot; SRC=&quot;http://ha.ckers.org/xss.js&quot;&gt;&lt;/SCRIPT&gt;" },
			// Test 84
			{
					"<SCRIPT a=\">\" '' SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>",
					"&lt;SCRIPT a=&quot;&gt;&quot; '' SRC=&quot;http://ha.ckers.org/xss.js&quot;&gt;&lt;/SCRIPT&gt;" },
			// Test 85
			{
					"<SCRIPT \"a='>'\" SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>",
					"&lt;SCRIPT &quot;a='&gt;'&quot; SRC=&quot;http://ha.ckers.org/xss.js&quot;&gt;&lt;/SCRIPT&gt;" },
			// Test 86
			{
					"<SCRIPT a=`>` SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>",
					"&lt;SCRIPT a=`&gt;` SRC=&quot;http://ha.ckers.org/xss.js&quot;&gt;&lt;/SCRIPT&gt;" },
			// Test 87
			{
					"<SCRIPT a=\">'>\" SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>",
					"&lt;SCRIPT a=&quot;&gt;'&gt;&quot; SRC=&quot;http://ha.ckers.org/xss.js&quot;&gt;&lt;/SCRIPT&gt;" },
			// Test 88
			{
					"<SCRIPT>document.write(\"<SCRI\");</SCRIPT>PT SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>",
					"&lt;SCRIPT&gt;document.write(&quot;&lt;SCRI&quot;);&lt;/SCRIPT&gt;PT SRC=&quot;http://ha.ckers.org/xss.js&quot;&gt;&lt;/SCRIPT&gt;" },
			// Test 89
			{ "<A HREF=\"htt	p://6&#9;6.000146.0x7.147/\">XSS</A>",
					"<a href=\"\">XSS</a>" },
			// Test 90
			{
					"<A HREF=\"javascript:document.location='http://www.google.com/'\">XSS</A>",
					"<a href=\"\">XSS</a>" },
			// Test 91
			{
					"<%3C&lt&lt;&LT&LT;&#60&#060&#0060&#00060&#000060&#0000060&#60;&#060;&#0060;&#00060;&#000060;&#0000060;&#x3c&#x03c&#x003c&#x0003c&#x00003c&#x000003c&#x3c;&#x03c;&#x003c;&#x0003c;&#x00003c;&#x000003c;&#X3c&#X03c&#X003c&#X0003c&#X00003c&#X000003c&#X3c;&#X03c;&#X003c;&#X0003c;&#X00003c;&#X000003c;&#x3C&#x03C&#x003C&#x0003C&#x00003C&#x000003C&#x3C;&#x03C;&#x003C;&#x0003C;&#x00003C;&#x000003C;&#X3C&#X03C&#X003C&#X0003C&#X00003C&#X000003C&#X3C;&#X03C;&#X003C;&#X0003C;&#X00003C;&#X000003C;\\x3c\\x3C\u003c\u003C",
					"&lt;%3C&lt&lt;&LT&LT;&#60&#060&#0060&#00060&#000060&#0000060&#60;&#060;&#0060;&#00060;&#000060;&#0000060;&#x3c&#x03c&#x003c&#x0003c&#x00003c&#x000003c&#x3c;&#x03c;&#x003c;&#x0003c;&#x00003c;&#x000003c;&#X3c&#X03c&#X003c&#X0003c&#X00003c&#X000003c&#X3c;&#X03c;&#X003c;&#X0003c;&#X00003c;&#X000003c;&#x3C&#x03C&#x003C&#x0003C&#x00003C&#x000003C&#x3C;&#x03C;&#x003C;&#x0003C;&#x00003C;&#x000003C;&#X3C&#X03C&#X003C&#X0003C&#X00003C&#X000003C&#X3C;&#X03C;&#X003C;&#X0003C;&#X00003C;&#X000003C;\\x3c\\x3C&lt;&lt;" } };

	@Test
	public void testSanitation() {
		for (String[] pair : INPUT_OUTPUT_PAIRS) {
			String input = pair[0];
			String expected = pair[1];

			String actual = HTMLSanitizer.sanitize(input);

			Assert.assertEquals(expected, actual);
		}
	}

	@Test
	public void testMobileConversion() {
		Assert.assertEquals(
				"Paragraphs to newlines",
				"Jouw moeder is fucking lelijk\nEcht waar\n",
				HTMLSanitizer
						.paragraphsToNewlines("<p>Jouw moeder is fucking lelijk</p><p>Echt waar</p>"));
		Assert.assertEquals(
				"Newlines to paragraphs",
				"<p>Jouw moeder is fucking lelijk</p><p>Echt waar</p>",

				HTMLSanitizer
						.newlinesToParagraphs("Jouw moeder is fucking lelijk\nEcht waar"));
	}
}
