import useRefreshToken from "../hooks/useRefreshToken.jsx";
import useAuth from "../hooks/useAuth.jsx";
import {useEffect, useState} from "react";
import { Outlet } from "react-router-dom";



const RefreshTokenInReload = () => {

    const {auth} = useAuth();
    const refresh = useRefreshToken();
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        const verifyRefreshToken = async () => {
            try{
                await refresh();
            }catch(error){
                console.error(error);
            }finally{
                setLoading(false);
            }
        }

        if (!auth?.accessToken) {
            verifyRefreshToken();
        } else {
            setLoading(false);
        }

    }, [])

    return loading ? <p>loading</p> : <Outlet />;
}

export default RefreshTokenInReload;