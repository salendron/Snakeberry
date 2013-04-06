/**
 * Snakeberry JavaScipt client
 */

function SnakeberryClient(args) {
    this.baseURL = args.baseURL;
    this.radiosList = $(args.radiosList);

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
            var li=$('<li>',{id:'radio-'+v.RadioId}).append(a);
            me.radiosList.append(li);
        });
    });
};

