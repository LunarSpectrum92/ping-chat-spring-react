import './App.css'
import LoginForm from './pages/Login.jsx';
import {AuthProvider} from "./contexts/AuthProvider.jsx";
import Register from './pages/Register.jsx';
import Users from './pages/Users.jsx';
import {Routes, Route, Link} from 'react-router-dom';
import Friends from './pages/Users.jsx';
import Private from './components/Private.jsx';
import Messages from './pages/Messages.jsx';
import RefreshTokenInReload from "./components/RefreshTokenInReload.jsx";
import Invitations from './pages/Invitations.jsx';



function App() {


  return (
    <>
        <AuthProvider>
            <Routes>
                <Route element={<RefreshTokenInReload />} >
                    <Route element={<Private />} >
                        <Route path="/" element={<Friends />} />
                        <Route path="/messages" element={<Messages />} />
                        <Route path="/users" element={<Users />} />
                        <Route path="/invitations" element={<Invitations />} />
                    </Route>
                </Route>
                <Route path="/login" element={<LoginForm />} />
                <Route path="/register" element={<Register />} />
            </Routes>
        </AuthProvider>
    </>
  )
}

export default App
