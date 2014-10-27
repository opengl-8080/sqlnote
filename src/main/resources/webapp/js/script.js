$(function() {
//    var jsEditor = CodeMirror.fromTextArea(document.getElementById('sqlEditor'), {
//        mode: "sql",
//        lineNumbers: true,
//        indentUnit: 4,
//        lineWrapping: false
//    });
    
    $('body').layout({
        applyDefaultStyles: true
    });
    
    
//    $('#body').on('keydown', function(e) {
//        if (e.keyCode === 9) {
//            e.preventDefault();
//            var elem = e.target;
//            var val = elem.value;
//            var pos = elem.selectionStart;
//            elem.value = val.substr(0, pos) + '    ' + val.substr(pos, val.length);
//            elem.setSelectionRange(pos + 4, pos + 4);
//        }
//    });
//    
//    $('#form').on('submit', function() {
//        var path = $('#path').val();
//        var body = $('#body').val();
//        var method = $('#method').val();
//        
//        $.ajax({
//            url: 'http://localhost:48123/sqlnote/api/' + path,
//            type: method,
//            data: body,
//            success: function(data) {
//                $('#response').text(JSON.stringify(data, null, 4));
//            },
//            error: function(xhr, status, error) {
//                var json = JSON.parse(xhr.responseText)
//                $('#response').text(JSON.stringify(json, null, 4));
//            }
//        });
//        
//        return false;
//    });
});
