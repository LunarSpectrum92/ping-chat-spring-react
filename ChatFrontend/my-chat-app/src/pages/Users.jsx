import Navbar from "../components/NavBar.jsx";
import usePrivateAxios from "../hooks/usePrivateAxios";
import UserField from "../components/UserField.jsx";
import {useEffect, useState} from "react";


const Users = () => {

    const [users, setUsers] = useState([]);
    const axiosPrivate = usePrivateAxios();


    useEffect(() => {
        const fetchData = async () => {
            try{
                const result = await axiosPrivate.get('/user/all');
                console.log(result.data)
                setUsers(result.data);
            }catch(e){
                console.error(e);
            }
        };
        fetchData();
    }, [])


    return (
        <div>
            <div>
                <Navbar>
            {
                users.length > 0 ?
                    users.map( user => (
                    <UserField {...user} key={user.id} style={{backgroundColor: "black"}} />
                )) : (<h2>Loading...</h2>)
            }
                </Navbar>
            </div>
        </div>
    );

}


export default Users;