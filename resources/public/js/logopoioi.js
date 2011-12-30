function focusIf(selector){
  if($(selector).length != 0){
    $(selector).focus();
  } 
}

function addTagCloud(){
  var tc_div = $("#tag_cloud");
  if(tc_div.length !=0){
    $.getJSON('/all_tags', function(data){
      $.each(data[0], function(k,v){
        var link = $("<a />");
        link.attr("href", '/bangtag/'+k.substring(1,k.length));
        link.text(k);
        link.addClass("bangtag_link");
        tc_div.append(link);
      });
    });
  }
}

$(document).ready(function(){
  focusIf("#the_box");
  focusIf("#pwd_input");

  $("#save").click(function(event){
    $("#the_form").submit();      
  });

  addTagCloud();

});


//Take from http://css-tricks.com/better-password-inputs-iphone-style/
$(function() {
  $("#login_form div").append("<div id='letterViewer'>");
  $("#pwd_input").keypress(function(e) {
    $("#letterViewer")
      .html(String.fromCharCode(e.which))
      .fadeIn(200, function() {
          $(this).fadeOut(200);
       });
  });
});
