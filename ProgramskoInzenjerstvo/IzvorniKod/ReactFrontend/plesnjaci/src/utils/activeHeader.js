export default function activeHeader(document, item){
        let list=document.querySelectorAll('.menu-item');
        for (let i=0;i<list.length;i++){
            if(list[i].getAttribute('aria-label')===item){
                list[i].className="menu-item active";
            }else{
                list[i].className="menu-item";
            }
        }
    }