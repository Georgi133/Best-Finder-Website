import { useNavigate } from 'react-router-dom';
import { useContext, createContext, useState } from 'react';
import { authServiceFactory } from '../services/AuthService';
import { useLocalStorage } from '../useLocalStorage/useLocalStorage';
// import { authServiceFactory } from '../services/authService';

export const AuthContext = createContext();

export const AuthProvider = ({
    children,
    userReload,

}) => {
    const [userInfo, setUserInfo] = useState({});
    const authService = authServiceFactory();
    const [token, setToken] = useLocalStorage('token', {});
    const[isLoggedOut, setIsLoggedOut] = useState(false);

    const navigate = useNavigate();

    const onChangePasswordSubmit =  async (data) => {
        await authService.changePassword(data);

        navigate('/');
    }

    const onEditSubmit = async (data) => {
        try {
            const result = await authService.edit(data);
            
            setUserInfo(result)

            navigate('/');
        } catch (error) {
            console.log('There is a problem');
        }
        
    }

    const onLoginSubmit = async (data) => {
        try {
            const result = await authService.login(data);
            
            const { token, ...userInfo } = result;
            setToken(token);

            setUserInfo(userInfo)
            setIsLoggedOut(false);

            navigate('/');
        } catch (error) {
            console.log('There is a problem');
        }
    };

    const onRegisterSubmit = async (values) => {
        console.log(values);
        const { confirmPassword, ...registerData } = values;
        if (confirmPassword !== registerData.password) {
            console.log('password doesnt match')
            return;
        }
        try {

             await authService.register(registerData);

            navigate('/users/login');
        } catch (error) {
            console.log('There is a problem');
        }
    };

    const onLogout = async () => {
        // await authService.logout();
        localStorage.removeItem('token');
        setUserInfo({});
        setIsLoggedOut(true)
    };

    const onProfileChange = async (data) => {
        const result = await authService.getUserInfo(data);
        setUserInfo(result);
    }

    const contextValues = {
        onRegisterSubmit,
        onLoginSubmit,
        onLogout,
        onChangePasswordSubmit,
        onProfileChange,
        onEditSubmit,
        userId: userInfo.id,
        userEmail: userInfo.email ? userInfo.email : isLoggedOut ? undefined : userReload.email,
        userFullName: userInfo.fullName,
        userRole: userInfo.role ? userInfo.role : isLoggedOut ? undefined : userReload.role,
        userAge: userInfo.age,

    }

    return(
            <AuthContext.Provider value={contextValues}>
                {children}
            </AuthContext.Provider>
    )

}

export const useAuthContext = () => {
    const context = useContext(AuthContext);

    return context;
};