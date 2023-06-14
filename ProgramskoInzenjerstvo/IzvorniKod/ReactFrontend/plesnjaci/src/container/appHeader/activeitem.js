export const activeitem= ()=>{
    let list=document.querySelectorAll('.menu-item');
    for(let i=0;i<list.length;i++){
        list[i].onclick=function(){
            for(let j=0;j<list.length;j++){
                list[j].className='menu-item';
            }
            list[i].className='menu-item active'
        }
    }
    return;
}

export const openmenu=()=>{
    let menubar=document.querySelector('.menubar');
        if(menubar.classList.contains('active')){
            localStorage.setItem("opensidebar", "false");
            menubar.className = 'menubar';
        }else{
            localStorage.setItem("opensidebar", "true");
            menubar.className = 'menubar active';
        }
}