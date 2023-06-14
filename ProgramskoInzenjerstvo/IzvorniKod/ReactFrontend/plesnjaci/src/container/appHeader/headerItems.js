
import { 
    IoHomeOutline,
    IoDocumentTextOutline,
    IoAccessibilityOutline,
    IoBookOutline,
    IoCalendarNumberOutline
} from "react-icons/io5";
export const getHeaderItems = [
    {
        label: "HOME PAGE",
        link: "/",
        icon: <IoHomeOutline/>
    },

    {
        label: "PROFILE",
        link: "/profile",
        icon:<IoAccessibilityOutline/>
    },

    {
        label: "MY CLUBS",
        link: "/clubs",
        icon:<IoDocumentTextOutline/>
    },
    {
        label: "MY CALENDAR",
        link: "/calendar",
        icon:<IoCalendarNumberOutline/>
    },
    {
        label: "ABOUT US",
        link: "/info",
        icon:<IoBookOutline/>
    },
]