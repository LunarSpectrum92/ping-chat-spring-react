import Navbar from "../components/Navbar";
import { useEffect, useState } from "react";
import usePrivateAxios from "../hooks/usePrivateAxios.jsx";
import FriendField from "../components/FriendField.jsx";
import Chat from "../components/Chat.jsx";
import InputField from "../components/InputField.jsx";


const Messages = () => {
    const [friends, setFriends] = useState([]);
    const [friendForBubble, setFriendForBubble] = useState([]);
    const [messages, setMessages] = useState([]);
    const [currentFriendId, setCurrentFriendId] = useState(null);
    const [isClicked, setIsClicked] = useState(false);
    const axiosPrivate = usePrivateAxios();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const result = await axiosPrivate.get('/user/friends');
                setFriends(result.data);
            } catch (e) {
                console.error(e);
            }
        };
        fetchData();
    }, []);

    const HandleClick = async (friend) => {
        try {
            console.log(friend.AuthId);
            const messFetch = await axiosPrivate.get(`/message/conversation/${friend.AuthId}`);
            setMessages(messFetch.data);
            setFriendForBubble(friend)
            setCurrentFriendId(friend.AuthId);
            setIsClicked(true);

            const drawerCheckbox = document.getElementById('my-drawer');
            if (drawerCheckbox) drawerCheckbox.checked = false;

        } catch (e) {
            setIsClicked(false);
            console.error(e);
        }
    };

    return (
        <Navbar>
            <div className="drawer lg:drawer-open h-full">
                <input id="my-drawer" type="checkbox" className="drawer-toggle" />

                <div className="drawer-content flex flex-col bg-base-100 overflow-hidden">

                    <div className="flex items-center p-2 lg:hidden bg-base-200">
                        <label htmlFor="my-drawer" className="btn btn-ghost btn-sm">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="inline-block w-6 h-6 stroke-current"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16"></path></svg>
                            Lista znajomych
                        </label>
                    </div>

                    <main className="flex-1 h-full overflow-hidden">
                        {console.log(friendForBubble)}
                        <Chat messages={messages} currentFriendId={currentFriendId} friend={friendForBubble}/>
                    </main>
                </div>

                <div className="drawer-side h-full z-20">
                    <label htmlFor="my-drawer" aria-label="close sidebar" className="drawer-overlay"></label>
                    <aside className="bg-base-100 w-80 min-h-full ">
                        <div className="overflow-y-auto h-full">
                            {friends.length > 0 ? (
                                friends.map(friend => (
                                    <FriendField
                                        {...friend}
                                        key={friend.id}
                                        onClick={() => HandleClick(friend)}
                                    />
                                ))
                            ) : (
                                <h2 className="p-4 text-accent-content">Ładowanie...</h2>
                            )}
                        </div>
                    </aside>
                </div>
            </div>
        </Navbar>
    );
}

export default Messages;