function createInsertButton(tx, pf, sf) {
    var text, prefix, suffix;

    function InsertButton(t, p, s) {
        text = t;
        prefix = p;
        suffix = s;
    }

    InsertButton.prototype.text = function (t) {
        if (!arguments.length) return text;
        text = t;
    }

    InsertButton.prototype.prefix = function (p) {
        if (!arguments.length) return prefix;
        prefix = p;
    }

    InsertButton.prototype.suffix = function (s) {
        if (!arguments.length) return suffix;
        suffix = s;
    }

    return new InsertButton(tx, pf, sf);
}

(function ($) {
    var buttons = [], idx = 1;

    buttons.push(createInsertButton('bold', '[b]', '[/b]'));
    buttons.push(createInsertButton('italic', '[i]', '[/i]'));
    buttons.push(createInsertButton('underline', '[u]', '[/u]'));
    buttons.push(createInsertButton('strike', '[s]', '[/s]'));
    buttons.push(createInsertButton('url', '[url]', '[/url]'));
    buttons.push(createInsertButton('url=', '[url=', '][/url]'));
    buttons.push(createInsertButton('quote', '[quote]', '[/quote]'));
    buttons.push(createInsertButton('quote=', '[quote=]', '[/quote]'));
    buttons.push(createInsertButton('image', '[img]', '[/img]'));
    buttons.push(createInsertButton('youtube', '[youtube]', '[/youtube]'));

    $.fn.bbedit = function () {
        return this.each(function (i, editor) {
            var buttonbar = '<br />', clickable = [];

            $(buttons).each(function (j, button) {
                var current = idx++, markupid;

                markupid = 'bbcode_btn_' + current;

                buttonbar = buttonbar + '<a id="' + markupid + '" class="bbbutton">' + button.text() + '</a>';
                clickable[current] = button;
            });

            $(editor).wrap('<div class="bbcode_editor"></div>');
            $(editor).after(buttonbar);

            $(clickable).each(function (j, button) {
                $('#bbcode_btn_' + j).click(function () {
                    $(editor).insertAroundCursor(button.prefix(), button.suffix());
                });
            });
        });
    };
}(jQuery));
