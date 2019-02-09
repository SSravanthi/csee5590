function getGithubInfo(user) {
    //1. Create an instance of XMLHttpRequest class and send a GET request using it. The function should finally return the object(it now contains the response!)
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        //if the response is successful show the user's details
        if (this.readyState == 4 && this.status == 200) {
                showUser(JSON.parse(this.responseText));  // JSON is Javascript object notation
            // that coverts the data from api to a readable format that any programming language could understand.
          }
        
    };
    xhttp.open("GET", "https://api.github.com/search/users?q="+user, true);
    xhttp.send(); 
}

function showUser(user) {
    console.log("user items",user.items); //displays the user details
    if(user.total_count>0){
        console.log(user.items);
        $('#profile').html('').append($('<h3></h3>').html(' (' + user.total_count + ' users found)'));
        $.each(user.items,function(key,val){
            $('#profile').append($('<h1></h1>').html(val.login));
            $('#profile').append($('<div  class="avatar"></div>').html("<img src='"+val.avatar_url+"'  width='60' height='60'/>"));
            $('#profile').append($('<div  class="information"></div>').html("User Repos URL : "+val.repos_url+"<br/ >"+"User Url : "+val.html_url+"<br /> User organizations URL : "+val.organizations_url));
        })
        // $("#profile").html(data);
    } else {
        //else display suitable message
        noSuchUser(user);
    }
    //2. set the contents of the h2 and the two div elements in the div '#profile' with the user content


}

function noSuchUser(username) {
    //3. set the elements such that a suitable message is displayed
    $('#profile').html($('<h1></h1>').html('NO Users Found !!'));


}


$(document).ready(function(){
    $(document).on('keypress', '#username', function(e){
        //check if the enter(i.e return) key is pressed
        if (e.which == 13) {
            //get what the user enters
            username = $(this).val();
            //reset the text typed in the input
            $(this).val("");
            //get the user's information and store the respsonse
            getGithubInfo(username);
            // response.status gets undefined because of API response it proceed to next step  
            // if (response.status == 200) {
            //     showUser(JSON.parse(response.responseText));
            //     //else display suitable message
            // } else {
            //     noSuchUser(username);
            // }
        }
    })
});
