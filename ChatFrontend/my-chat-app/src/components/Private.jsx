import useAuth from '../hooks/useAuth';
import {useLocation, Navigate, Outlet} from 'react-router-dom';




const Private = () => {
    const {auth} = useAuth();
    const location = useLocation();
    if (auth?.accessToken) {
        return <Outlet />;
    }
    return <Navigate to="/login" state={{ from: location }} replace />;
}

export default Private;