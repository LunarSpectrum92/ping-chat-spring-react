import {authPublic} from '../api/auth';
import useAuth from './useAuth';



const useRefreshToken = () => {

    const {setAuth} = useAuth();

    const refresh = async () => {
        const response = await authPublic.post('/refresh');
        setAuth( prev => {
            return {...prev, accessToken: response.data.token, AuthId: response.data.AuthId };
        })
        return response.data;
    }

    return refresh;
}


export default useRefreshToken;