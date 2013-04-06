/**
 * Snakeberry JavaScipt client
 */

function SnakeberryClient(args) {
    this.baseURL = args.baseURL;
    this.radiosList = $(args.radiosList);
    this.nowplaying = null;
    this.radios = { };

    this.updateCurrentRadio();
    this.updateRadios();
}

SnakeberryClient.prototype.updateRadios = function() {
    var me = this;
    $.ajax( this.baseURL + 'radios' ).success(function(json) {
        // TODO: error handling
        var radios = json.ResponseData.Radios; // TODO: catch
        me.radiosList.empty();
        me.radios = {};
        $.each(radios,function(k,v){
            me.radios[v.RadioId] = v.DisplayName;
            var a=$('<a>').text(v.DisplayName);
            var id = me.hashCode(v.StreamUrl); // MediaURI ?!
            var li=$('<li>',{id:'radio-'+id}).append(a);
            if (me.nowplaying && me.nowplaying == v.RadioId) {
                li.addClass('selected');
            }
            me.radiosList.append(li);
        });
    });
};

SnakeberryClient.prototype.updateCurrentRadio = function() {
    var me = this;
    $.ajax( this.baseURL + 'radio/nowplaying' ).success(function(json) {
        var response = json.ResponseData;
        var id = me.hashCode(response.MediaURI);
        me.radiosList.find('.selected').removeClass('selected');
        $('#radio-'+id).addClass('selected');
    });
};

// implements Java.lang.String.hashCode()
SnakeberryClient.prototype.hashCode = function(s){
    var hash = 0;
    if (s == undefined || !s.length) return hash;
    for (i = 0; i < s.length; i++) {
        hash = ((hash<<5)-hash) + s.charCodeAt(i);
        hash = hash & hash; // 32bit int
    }
    return hash;
};

