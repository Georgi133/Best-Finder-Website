import { useNavigate } from "react-router-dom";
import { useContext, createContext, useState } from "react";
import { authServiceFactory } from "../services/AuthService";
import { useLocalStorage } from "../useLocalStorage/useLocalStorage";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
// import { authServiceFactory } from '../services/authService';

export const AuthContext = createContext();

export const AuthProvider = ({ children, userReload }) => {
  const [userInfo, setUserInfo] = useState({});
  const authService = authServiceFactory();
  const [token, setToken] = useLocalStorage("token", {});
  const [isLoggedOut, setIsLoggedOut] = useState(false);
  const [errorMessage , setErrorMessage] = useState('');
  const [errorNumber , setErrorNumber] = useState(0);
  const [serverErrors, setServerErrors] = useState({});
  const [successfullySendedPasswordOnEmail , setsuccessfullySendedPasswordOnEmail] = useState(false);
  const [registerSuccess , setRegisterSuccess] = useState(false);
  const [changedPasswordSuccess , setChangedPasswordSuccess] = useState(false);
  const [isProfileEdited , setIsProfileEdited] = useState(false);
   

  const navigate = useNavigate();

  const onChangePasswordSubmit = async (data) => {
    try{
      const result = await authService.changePassword(data);
      setChangedPasswordSuccess(result.created);
    } catch (error) {
      ifServerThrowNavigate(error);
      setErrorNumber(convertErrorStringInNumber(error));
        const rawMessage = convertResponseMessage(error);
        messageOrFieldChecker(rawMessage);   
    }
    
  };

  const onEditSubmit = async (data) => {
    try {
      const result = await authService.edit(data);
      setIsProfileEdited(result.age ? true : false);

      setUserInfo(result);

    } catch (error) {
      ifServerThrowNavigate(error);
      setErrorNumber(convertErrorStringInNumber(error));
        const rawMessage = convertResponseMessage(error);
        messageOrFieldChecker(rawMessage);  
    }
  };

  const onLoginSubmit = async (data) => {
    try {
      const result = await authService.login(data);

      const { token, ...userInfo } = result;

      if (token) {
        setToken(token);
      }
      setUserInfo(userInfo);
      setIsLoggedOut(false);

      navigate("/");
    } catch (error) {
      ifServerThrowNavigate(error);
      setErrorNumber(convertErrorStringInNumber(error));
      const rawMessage = convertResponseMessage(error);
      messageOrFieldChecker(rawMessage);
    }
  };

  const onForgottenPasswordEmail = async (data) => {
    try {
      const result = await authService.forgottenPassword(data);
      setsuccessfullySendedPasswordOnEmail(result.created);
    } catch (error) {
      ifServerThrowNavigate(error);
      setErrorNumber(convertErrorStringInNumber(error));
      const rawMessage = convertResponseMessage(error);
      messageOrFieldChecker(rawMessage);  
    }
  };

  const ifServerThrowNavigate = (error) => {
    if(error.message === 'forbidden') {
      navigate('/not-allowed')
      return;
    }
    if(error.message === 'Failed to fetch') {
      navigate('/server-error');
    }
  }


  const messageOrFieldChecker = (rawMessage) => {
    const index = rawMessage.indexOf(':');
      const nameOfField = rawMessage.substring(0, index); 
      if(nameOfField === 'message') {
        const message = rawMessage.substring(index + 1);
        setErrorMessage(message);
      }else {
        validFieldChecker(rawMessage);
      }
  }

  const convertErrorStringInNumber = (errorString) => {
    return Number(errorString.message.substring(errorString.message.length - 4))
  }

  const convertResponseMessage = (message) => {
    return message.message.substring(0, message.message.length - 4);
  }

  const validFieldChecker = (values) => {
    let arr = values.split(',');

    const errors = arr.reduce(function(acc, arr) {
      let newArray = arr.split(':');
      return{
        ...acc,
        [newArray[0]]:newArray[1],
      };
    }, {});
    
    setErrorMessage(Object.keys(errors).length !== 0 ? "" : errorMessage);
     setServerErrors(errors);
  }

  const onRegisterSubmit = async (values) => {
    const { confirmPassword, ...registerData } = values;
    try {
      const result = await authService.register(registerData);
      setRegisterSuccess(result.token ? true : false);
    } catch (error) {
      setErrorNumber(convertErrorStringInNumber(error));
        const rawMessage = convertResponseMessage(error);
        messageOrFieldChecker(rawMessage);   
    }
  };

 

  const onLogout = async () => {
    // await authService.logout();
    localStorage.removeItem("token");
    setUserInfo({});
    setIsLoggedOut(true);
  };

  const onProfileChange = async (data) => {
    const result = await authService.getUserInfo(data);

    setUserInfo(result);
  };

  const contextValues = {
    onRegisterSubmit,
    onLoginSubmit,
    onLogout,
    onChangePasswordSubmit,
    onProfileChange,
    onEditSubmit,
    setErrorMessage,
    setServerErrors,
    onForgottenPasswordEmail,
    setsuccessfullySendedPasswordOnEmail,
    setRegisterSuccess,
    setIsProfileEdited,
    setChangedPasswordSuccess,
    isProfileEdited,
    changedPasswordSuccess,
    registerSuccess,
    successfullySendedPasswordOnEmail,
    serverErrors,
    errorMessage,
    userId: userInfo.id,
    userEmail: userInfo.email
      ? userInfo.email
      : isLoggedOut
      ? undefined
      : userReload.email,
    userFullName: userInfo.fullName,
    userRole: userInfo.role
      ? userInfo.role
      : isLoggedOut
      ? undefined
      : userReload.role,
    userAge: userInfo.age,
  };

  return (
    <AuthContext.Provider value={contextValues}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuthContext = () => {
  const context = useContext(AuthContext);

  return context;
};
