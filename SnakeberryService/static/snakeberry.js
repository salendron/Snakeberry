/**
 * Snakeberry JavaScipt client
 */

// A radio station
function Radio(r) {
    this.name   = r.DisplayName;
    this.stream = r.MediaURI ? r.MediaURI : r.StreamUrl;
    this.hash   = this.hashCode(this.stream);
    this.id     = r.RadioId;
}

Radio.prototype.listItem = function(player) {
    var radio = this;
    var a  = $('<a>').text(radio.name);
    var li = $('<li>',{id:'radio'+radio.hash}).append(a);
    return li;
}

// implements Java.lang.String.hashCode()
Radio.prototype.hashCode = function(s) {
    var hash = 0;
    if (s == undefined || !s.length) return hash;
    for (i = 0; i < s.length; i++) {
        hash = ((hash<<5)-hash) + s.charCodeAt(i);
        hash = hash & hash; // 32bit int
    }
    return hash;
};

// A Radio player client
function SnakeberryClient(args) {
    var player = this;
    this.baseURL = args.baseURL;
    this.radiosList = $(args.radiosList);
    this.volumeControl = $(args.volumeControl);
    this.volumeControl.change(function(){
//        player.setVolume($(this).val());
    });
    this.playing = null;
    this.radios = { };
    this.updateRadios();
    this.updatePlaying();
//    this.updateVolume();
}

SnakeberryClient.prototype.playRadio = function(radio) {
    var player = this;
    $.ajax( player.baseURL + 'radio/play/' + radio.id ).success(function(json){
        player.playing = radio;
        player.showPlaying();
    });
};

SnakeberryClient.prototype.stopPlaying = function() {
    var player = this;
    $.ajax( player.baseURL + 'radio/stop' ).success(function(json){
        player.playing = null;
        player.showPlaying();
    })
};

SnakeberryClient.prototype.showPlaying = function() {
    var player = this;
    player.radiosList.find('.active').each(function(){
        $(this).removeClass('active');
        $(this).off('click');
        var hash = $(this).attr('id').substring(5);
        var radio = player.radios[hash];
        $(this).click(function(){ player.playRadio(radio) });
    });
    if (player.playing) {
        $('#radio'+player.playing.hash).addClass('active').click(
            function(){player.stopPlaying()}
        );
    }
};

SnakeberryClient.prototype.updateRadios = function() {
    var player = this;
    $.ajax( player.baseURL + 'radios' ).success(function(json) {
        player.radiosList.empty();
        player.radios = {};
        $.each(json.ResponseData.Radios,function(k,r){
            var radio = new Radio(r);
            player.radios[radio.hash] = radio;
            var li = radio.listItem();
            if (player.playing && player.playing.hash == radio.hash) {
                li.addClass('active');
                li.click(function(){player.stopPlaying()});
            } else {
                li.click(function(){player.playRadio(radio)});
            }
            player.radiosList.append(li);
        });
    });
};

SnakeberryClient.prototype.updatePlaying = function() {
    var player = this;
    $.ajax( player.baseURL + 'radio/nowplaying' ).success(function(json) {
        player.playing = new Radio(json.ResponseData);
        player.showPlaying();
    });
};

SnakeberryClient.prototype.updateVolume = function() {
    var player = this;
    $.ajax( player.baseURL + 'getvolume' ).success(function(json) {
        var volume = json.ResponseData;
        player.volumeControl.val(volume);
    });
};

SnakeberryClient.prototype.setVolume = function(volume) {
    var player = this;
    $.ajax( player.baseURL + 'setvolume/' + volume ).success(function(json) {
        player.volumeControl.val(volume);
    });
};

