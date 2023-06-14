import Resizer from "react-image-file-resizer";
const resizeFile = (file) =>
    new Promise((resolve) => {
        Resizer.imageFileResizer(file, 200, 200, "JPEG", 90, 0,
            (uri) => {
                resolve(uri);
            },
            "file"
        );
    });

function setPhoto(state, setState, photo) {
    setState({
        ...state,
        slika: photo
    });
}
    
export async function handleFileChange(e, state, setState) {

    if (e.target.files[0]) {
        let file = await resizeFile(e.target.files[0]);
        let reader = new FileReader();

        reader.readAsDataURL(file);

        reader.onload = () => {
            setPhoto(state, setState, reader.result);
        }
        reader.onerror = () => {
            setPhoto(state, setState, "");
        }
    }
    else {
        setPhoto(state, setState, "");
    }
}