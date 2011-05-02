var RADIUS = 180,
MAJOR_LINE_LENGTH = 15,
MINOR_LINE_LENGTH = 10,
CENTER_X = 210,
CENTER_Y = 210,
MISS_DELAY = 800,
CORRECT_DELAY = 500,
EASY=/\#easy$/.exec(window.location);
var correctAnswer = ["correctAnswer", "translatedAnswer"];//[0,0];
var messageTimer = null, gameTimeTimer = null;
var correct = 0, missed = 0;
var feedback = false;
var timeRemaining = 180;

var choices = [["choice1", "translatedChoice1"], ["choice2","translatedChoice2"], ["choice3", "translatedChoice3"]];

var imgurl = "http://catsinsinks.com/images/cats/rotator.php",
wordImage = new Image();

function loadCard() {
    //TODO: grab from the website
    imgurl = "http://catsinsinks.com/images/cats/rotator.php?" +Math.random();
    wordImage = new Image();
    wordImage.src = imgurl;
    wordImage.onload =
	function() {
	    var ctx = document.getElementById('game')
			  .getContext('2d');
	    ctx.drawImage(wordImage,0,0,
		      ctx.canvas.width, ctx.canvas.height);
    };
    correctAnswer = ["correct", "translatedAnswer"];
    choices = [["choice1", "translatedChoice1"], ["choice2","translatedChoice2"], ["choice3", "translatedChoice3"]];

}

function parseInt1( str ) {
    while (str.charAt(0) == '0') str = str.substring(1);
    return parseInt(str);
}

function Chooser() {
    var choices=[], weights=[0], totalWeight=0, allpublic={};

    var add = allpublic.add = function( obj, weight ) {
	choices.push(obj);
	weights.push( totalWeight += weight );
	return allpublic;
    };

    var choose = allpublic.choose = function() {
	if ( choices.length == 0 )
	    return null;
	var choice = Math.random() * totalWeight, start=0, end=weights.length-1, next;
	while ( end > start + 1 ) {
	    next=start+Math.floor((end - start)/2);
	    if ( choice >= weights[next] )
		start = next;
	    else
		end = next;
	}
	return choices[start];
    };
    return allpublic;
}

function drawGame( ) {
    var ctx = document.getElementById('game').getContext('2d');
    ctx.clearRect(0,0,ctx.canvas.width,ctx.canvas.height);

    ctx.drawImage(wordImage,0,0,
		      ctx.canvas.width, ctx.canvas.height);
   /* img.onload = function() {

	console.log("drew:"+imgurl);
	//message("OKAY.","rgb(0,0,0)", 500);
    };*/
/*
    ctx.strokeStyle = 'rgb(0,0,0)';
    ctx.fillStyle = 'rgb(0,0,0)';
    ctx.lineWidth = 4;
    ctx.beginPath();
    ctx.arc( CENTER_X, CENTER_Y, RADIUS, 0, Math.PI*2, false );
    ctx.closePath();
    ctx.stroke();


    ctx.lineWidth = 1;
    for ( var i=0; i < 60; i++ ) {
	rotate( ctx, Math.PI*i/30.0, function(ctx) {
		    ctx.moveTo( 0, -RADIUS );
		    ctx.lineTo( 0, -RADIUS + MINOR_LINE_LENGTH );
		    ctx.stroke();
		});
    }

    ctx.lineWidth = 4;
    for ( var i=0; i < 12; i++ ) {
	rotate( ctx, Math.PI*i/6.0, function(ctx) {
		    ctx.moveTo( 0, -RADIUS );
		    ctx.lineTo( 0, -RADIUS + MAJOR_LINE_LENGTH );
		    ctx.stroke();
		});
    }

    for ( var i=1; i <= 12; i++ ) {
	if ( i <= 3 || i >= 9 ) {
	    rotate( ctx, Math.PI*i/6.0, function(ctx) {
			ctx.font = 'bold 16pt Helvetica,Arial';
			ctx.fillText(i+'', -ctx.measureText(i+'').width/2.0, -RADIUS + 35 );
			if ( EASY ) {
			    ctx.font = 'normal 9pt Helvetica,Arial';
			    ctx.fillText((i*5)+'', -ctx.measureText((i*5)+'').width/2.0, -RADIUS + 50 );
			}
		    });
	} else {
	    rotate( ctx, -Math.PI*(6-i)/6.0, function(ctx) {
			ctx.font = 'bold 16pt Helvetica,Arial';
			ctx.fillText(i+'', -ctx.measureText(i+'').width/2.0, RADIUS - 20 );
			if ( EASY ) {
			    ctx.font = 'normal 9pt Helvetica,Arial';
			    ctx.fillText((i*5)+'', -ctx.measureText((i*5)+'').width/2.0, RADIUS - 40 );
			}
		    });
	}
    }

    ctx.fillStyle = 'rgb(180,0,0)';
    rotate( ctx, Math.PI * minutes/30.0, function(ctx) {
		ctx.moveTo(-5,0);
		ctx.lineTo(-2,-RADIUS*.8);
		ctx.lineTo(2,-RADIUS*.8);
		ctx.lineTo(5,0);
		ctx.fill();
	    });

    ctx.fillStyle = 'rgb(180,0,0)';
    rotate( ctx, Math.PI * (hours + minutes/60.0)/6.0, function(ctx) {
		ctx.moveTo(-5,0);
		ctx.lineTo(-3,-RADIUS/2);
		ctx.lineTo(3,-RADIUS/2);
		ctx.lineTo(5,0);
		ctx.fill();
	    });

    ctx.fillStyle = 'rgb(0,0,0)';
    ctx.beginPath();
    ctx.arc( CENTER_X, CENTER_Y, 8, 0, Math.PI*2, false );
    ctx.closePath();
    ctx.fill();*/
}

function rotate( ctx, r, drawf ) {
    ctx.save();
    ctx.beginPath();
    ctx.translate( CENTER_X, CENTER_Y );
    ctx.rotate( r  );
    ctx.beginPath();
    drawf( ctx );
    ctx.closePath();
    ctx.restore();
}

function choose( options, n ) {
    var choices = [].concat(options), selections = [];
    for ( var i=0; i < n; i++ )
	selections.push( choices.splice(Math.floor(Math.random()*choices.length),1)[0] );
    return selections;
}

function generateTimes() {
    permutations = []
    var t1 = [Math.floor(Math.random()*12) + 1,
	      EASY ? Math.floor(Math.random()*12)*5 : Math.floor(Math.random()*60)];
    permutations.push( normalize(t1) );
    permutations.push( normalize([t1[0]-1,t1[1]]) );
    permutations.push( normalize([t1[0]+1,t1[1]]) );
    permutations.push( normalize([t1[0],t1[1]-5]) );
    permutations.push( normalize([t1[0],t1[1]+5]) );
    permutations.push( normalize([t1[0]+1,t1[1]-5]) );
    return permutations;
}

function normalize( time ) {
    time[0] = time[0] < 1 ? 12 + time[0]%12 : (time[0]-1)%12 + 1;
    time[1] = time[1] < 0 ? 60 + time[1]%60 : time[1]%60;
    return time;
}

function message( text, fillStyle, delay , size) {
    if ( messageTimer ) {
	clearTimeout( messageTimer );
	clearMessage();
    }
    var ctx = document.getElementById('game').getContext('2d');
    ctx.fillStyle = fillStyle;
    ctx.strokeStyle = 'rgb(255,255,255)';
    ctx.lineWidth = 3.0;
    if (!size) size = 30;
    ctx.font =  'bold ' + size +'pt Helvetica,Arial';
    //console.log(ctx);
        ctx.beginPath();


    var texts = text.split("\n");
    //console.log(texts);
    var totalHeight = texts.length * size;
    for ( var i = 0; i < texts.length; i++) {
	var text = texts[i];
	//console.log(text);
	var metrics = ctx.measureText(text);
	var start = (ctx.canvas.width - metrics.width)/2.0;
	ctx.fillText(text, start, (ctx.canvas.height - totalHeight) / 2 + size * i);
	ctx.strokeText(text, start, (ctx.canvas.height - totalHeight) / 2 + size * i);
    }
    //ctx.fillText(text, start, 240 );
    if ( delay )
	messageTimer = setTimeout( clearMessage, delay );
}

function clearMessage() {
    drawGame();
}

function testAnswer( choice ) {
    if ( feedback || timeRemaining < 1 )
	return;
    if (( choice == correctAnswer[0] )) {
	message("Correct!\n" + correctAnswer[0] +"\n=\n" + correctAnswer[1], 'rgba(80,60,120,.5)' );
	setTimeout( round, CORRECT_DELAY );
	feedback = true;
	document.getElementById("correct").innerHTML = ''+(++correct);
    } else {
	message("Nope.\n\n" + choice + "\n=\n" + findTranslation(choice), 'rgba(180,80,80,.5)', MISS_DELAY);
	document.getElementById("missed").innerHTML = ''+(++missed);
    }
}

function findTranslation(choice) {
    for (var i = 0; i < choices.length; i++) {
	if (choices[i][0] == choice) {
	    return choices[i][1];
	}
    }
    return "";
};

function init() {
    document.getElementById("answers").onclick = function(e) {
	if (!e)
	    var e = window.event;
	var target;
	if (e.target) target = e.target;
	else if (e.srcElement) target = e.srcElement;

	if ( target.nodeName == 'BUTTON' ) {
	    target.blur();
	    var choice = target.innerHTML;
	    testAnswer( choice );
	}
    };
    gameTimeTimer = setInterval( timeGame, 1000 );

    round();
}

function timeGame() {
    timeRemaining--;
    if ( timeRemaining < 1 ) {
	clearInterval( gameTimeTimer );
	document.getElementById('answers').innerHTML = '';
    }

    var seconds = timeRemaining % 60;
    var time = Math.floor(timeRemaining/60) + ':' + (seconds < 10 ? '0' : '' ) + seconds;
    document.getElementById('time-remaining').innerHTML = time;
}

function generateOptions() {
    var options = [];
    for (var i = 0; i < choices.length; i++)
	options.push(choices[i][0]);
    options.push(correctAnswer[0]);
    return choose(options, choices.length+1);
}

function round() {
    feedback = false;
    if ( timeRemaining < 1 )
	return;
    loadCard();
    document.getElementById('title').innerHTML = correctAnswer[0];
    var options = generateOptions() ;
    //var times = generateTimes().concat(generateTimes()).concat(generateTimes());
    //var options = choose( times, 5 );

    //var pad = function(x) { return (x < 10 ? '0' : '') + x };

    var buttons = '';
    for ( var i=0; i < options.length; i++ )
	buttons += "<button>" + options[i] + "</button>";
    document.getElementById('answers').innerHTML = buttons;

    //correctAnswer = choose(options, 1)[0];
    //drawGame( correctAnswer[0], correctAnswer[1] ); choose(options, 1)[0];
    drawGame();
}
