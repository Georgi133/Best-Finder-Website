import { useNavigate } from 'react-router-dom';
import { useContext, createContext } from 'react';
// import { useLocalStorage } from '../hooks/useLocalStorage';
// import { authServiceFactory } from '../services/authService';

const baseUrl = 'http://localhost:8080'
export const AuthContext = createContext();

export const AuthProvider = ({
    children,
}) => {
    // const [user, setUser] = useLocalStorage('user', {});

    const navigate = useNavigate();

    const onRegisterSubmit = async (values) => {
        console.log(values);
        const { confirmPassword, ...registerData } = values;
        if (confirmPassword !== registerData.password) {
            console.log('password doesnt match')
            return;
        }
        try {
            console.log('fetch')
            const result = fetch(`${baseUrl}/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(registerData)

            });

            // setUser(result);
            navigate('/');
        } catch (error) {
            console.log('There is a problem');
        }
    };


    const contextValues = {
        onRegisterSubmit,
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