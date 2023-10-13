import { useNavigate } from 'react-router-dom';
import { useContext, createContext } from 'react';
import { authServiceFactory } from '../services/AuthService';
import { useLocalStorage } from '../useLocalStorage/useLocalStorage';
// import { authServiceFactory } from '../services/authService';

export const AuthContext = createContext();

export const AuthProvider = ({
    children,
}) => {
    const authService = authServiceFactory();
    const [user, setUser] = useLocalStorage('user', {});

    const navigate = useNavigate();

    const onLoginSubmit = async (data) => {
        try {
            const result = await authService.login(data);

            setUser(result);

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

            navigate('/login');
        } catch (error) {
            console.log('There is a problem');
        }
    };

    const onLogout = async () => {
        await authService.logout();

        setUser({});
    };


    const contextValues = {
        onRegisterSubmit,
        onLoginSubmit,
        onLogout,
        userId: user.id,
        userEmail: user.email,
        token: user.token

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