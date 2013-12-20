$(document).ready(function() {
	$('.youtube-replace').each(function(i,o) {
		var vid = $(o).text();
		var url = '<iframe id=\"ytplayer-'+ 
			vid +'\" type=\"text/html\" width=\"560\" height=\"315\" src=\"http://www.youtube.com/embed/'+
			vid +'?autoplay=0&origin=https://www.tysanclan.com\" />';
		$(o).replaceWith(url);
	});
});