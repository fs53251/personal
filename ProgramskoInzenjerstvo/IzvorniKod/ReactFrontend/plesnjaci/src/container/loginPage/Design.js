import styled from 'styled-components';


 export const Container = styled.div`
 background-color: #fff;
 border-radius: 10px;
 box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
 position:relative;
 left:520px;
 top:80px;
 overflow: hidden;
 width: 678px;
 max-width: 100%;
 min-height: 700px;
 align-items:center;
 text-align-center;

 `;

 export const SignUpContainer = styled.div`
  position: absolute;
  top: 0;
  height: 100%;
  transition: all 0.6s ease-in-out;
  left: 0;
  width: 50%;
  opacity: 0;
  z-index: 1;
  align-items:center;
  text-align-center;
  ${props => props.signinIn !== true ? `
    transform: translateX(100%);
    opacity: 1;
    z-index: 5;
    align-items:center;
    text-align-center;
  ` 
  : null}
 `;
 

 export const SignInContainer = styled.div`
 position: absolute;
 top: 0;
 height: 100%;
 transition: all 0.6s ease-in-out;
 left: 0;
 width: 50%;
 z-index: 2;
 align-items:center;
 text-align-center;
 ${props => (props.signinIn !== true ? `transform: translateX(100%);` : null)}
 `;
 
 export const Form = styled.form`
 background-color: #ffffff;
 display: flex;
 align-items: center;
 justify-content: center;
 flex-direction: column;
 padding: 0 50px;
 height: 100%;
 text-align: center;

 `;
 
 export const Title = styled.h1`
 font-weight: bold;
 margin: 0;
 align-items:center;
 text-align-center;
 `;

 export const Title1 = styled.h1`
 font-weight: bold;
 margin: 0;
 align-items:center;
 text-align-center;
 position:relative;
 left:50px;
 `;
 export const Title2 = styled.h1`
 font-weight: bold;
 margin: 0;
 align-items:center;
 text-align-center;
 position:relative;
 right:45px;
 `;
 
 export const Input = styled.input`
 background-color: #eee;
 border: none;
 padding: 10px 12px;
 margin: 8px 0;
 width: 100%;
 `;
 

 export const Button = styled.button`
    border-radius: 20px;
    border: 1px solid rgb(174, 4, 174);
    background-color: rgb(174, 4, 174);
    color: #ffffff;
    font-size: 12px;
    font-weight: bold;
    padding: 12px 45px;
    letter-spacing: 1px;
    text-transform: uppercase;
    transition: transform 80ms ease-in;
    &:active{
        transform: scale(0.95);
    }
    &:focus {
        outline: none;
    }
    align-items:center;
    text-align-center;
 `;
 export const GhostButton = styled(Button)`
 background-color: transparent;
 border-color: #ffffff;
 align-items:center;
 text-align-center;
 position:relative;
 left:50px;
 `;

 export const GhostButton2 = styled(Button)`
 background-color: transparent;
 border-color: #ffffff;
 align-items:center;
 text-align-center;
 position:relative;
 right:50px;
 `;
 

 export const OverlayContainer = styled.div`
position: absolute;
top: 0;
left: 50%;
width: 50%;
height: 100%;
overflow: hidden;
transition: transform 0.6s ease-in-out;
z-index: 100;
align-items:center;
text-align-center;
${props =>
  props.signinIn !== true ? `transform: translateX(-100%);` : null}
`;

export const Overlay = styled.div`
background: rgb(174, 4, 174);
background-repeat: no-repeat;
background-size: cover;
background-position: 0 0;
color: #ffffff;
position: relative;
left: -100%;
height: 100%;
width: 200%;
transform: translateX(0);
transition: transform 0.6s ease-in-out;
align-items:center;
text-align-center;
${props => (props.signinIn !== true ? `transform: translateX(50%);` : null)}
`;
 
 export const OverlayPanel = styled.div`
     position: absolute;
     display: flex;
     align-items: center;
     justify-content: center;
     flex-direction: column;
     padding: 0 40px;
     text-align: center;
     top: 0;
     height: 100%;
     width: 50%;
     transform: translateX(0);
     transition: transform 0.6s ease-in-out;
     align-items:center;
     text-align-center;
 `;

 export const LeftOverlayPanel = styled(OverlayPanel)`
   transform: translateX(-20%);
   align-items:center;
   text-align-center;
   ${props => props.signinIn !== true ? `transform: translateX(0);` : null}
 `;

 export const RightOverlayPanel = styled(OverlayPanel)`
     right: 0;
     transform: translateX(0);
     align-items:center;
     text-align-center;
     ${props => props.signinIn !== true ? `transform: translateX(20%);` : null}
 `;

 export const Paragraph = styled.p`
 font-size: 14px;
   font-weight: 100;
   line-height: 20px;
   letter-spacing: 0.5px;
   margin:40px 50px 30px;
   position:relative;
   left:50px;

 `;

 export const Paragraph2 = styled.p`
   font-size: 14px;
   font-weight: 100;
   line-height: 20px;
   letter-spacing: 0.5px;
   margin:40px 50px 30px;
   position:relative;
   right:50px;

 `;

 export const Line = styled.p`
 display:flex;
 flex-direction:row;
 justify-content:center;
 font-size:14px;
 `;

