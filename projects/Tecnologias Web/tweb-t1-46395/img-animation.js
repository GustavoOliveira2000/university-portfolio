document.addEventListener('DOMContentLoaded', function() {

const imgs = document.getElementById('imgs');

const img = document.querySelectorAll('#imgs img'); 
let index = 0 ;

// console.log(img);

let interval = setInterval(run, 3000);

function run()
{
    index++;
    changeImgage()
}

function changeImgage()
{
    if(index > img.length - 1 )
    {
        index = 0 ;
    } else if (index < 0)
    {
        index = img.length - 1 ; 
    }

    imgs.style.transform = `translateY(${-index * 27.2}vw)`  
}

});