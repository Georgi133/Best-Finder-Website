import { createContext, useContext, useState } from "react";
import { adminServiceFactory } from "../services/AdminService";
import { useAuthContext } from "../AuthContext/AuthContext";
import { useNavigate } from "react-router-dom";

export const AdminContext = createContext();

export const AdminProvider = ({ children }) => {
  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState({});
  const adminService = adminServiceFactory();
  const { userRole, onLogout } = useAuthContext();
  const [errorMessageAdmin , setErrorMessageAdmin] = useState('');
  const [errorNumberAdmin , setErrorNumberAdmin] = useState(0);
  const [serverErrorsAdmin, setServerErrorsAdmin] = useState({});
  const [isChanged, setIsChanged] = useState(false);

  const onChangeFindUserSubmit = async (data) => {
    if (!data.currentUserRole) {
      data.currentUserRole = userRole;
    }
    try {
      const result = await adminService.findUserByEmail(data);
      setUserInfo(result);
    } catch (error) {
      ifServerThrowNavigate(error);
      setErrorNumberAdmin(convertErrorStringInNumber(error));
        const rawMessage = convertResponseMessage(error);
        messageOrFieldChecker(rawMessage);   
    }
      
  };

  const ifServerThrowNavigate = (error) => {
    if(error.message === 'forbidden' || error.message.includes('You are black listed')) {
      onLogout();
      navigate('/not-allowed')
      return;
    }
    if(error.message === 'Failed to fetch') {
      navigate('/server-error');
    }
  }

  const resetPage = () => {
    setUserInfo({});
  }

  const onClickChangeRole = async (data) => {
    const { setChangeRole, ...userInfo } = data;
    try{
      const result = await adminService.changeRole(userInfo);
      setUserInfo(result);
    setChangeRole(false);
    setIsChanged(result ? true : false);
    } catch (error) {
      ifServerThrowNavigate(error);
      setErrorNumberAdmin(convertErrorStringInNumber(error));
        const rawMessage = convertResponseMessage(error);
        messageOrFieldChecker(rawMessage);   
    }
    
    
  };

  const convertResponseMessage = (message) => {
    return message.message.substring(0, message.message.length - 4);
  };

  const convertErrorStringInNumber = (errorString) => {
    return Number(errorString.message.substring(errorString.message.length - 4))
  }

  const messageOrFieldChecker = (rawMessage) => {
    const index = rawMessage.indexOf(':');
      const nameOfField = rawMessage.substring(0, index); 
      if(nameOfField === 'message') {
        const message = rawMessage.substring(index + 1);
        setErrorMessageAdmin(message);
      }else {
        validFieldChecker(rawMessage);
      }
  };

  const validFieldChecker = (values) => {
    let arr = values.split(',');

    const errors = arr.reduce(function(acc, arr) {
      let newArray = arr.split(':');
      return{
        ...acc,
        [newArray[0]]:newArray[1],
      };
    }, {});
    
    setErrorMessageAdmin(Object.keys(errors).length !== 0 ? "" : errorMessageAdmin);
     setServerErrorsAdmin(errors);
  }

  const onClickBanUser = async (data) => {
    try{
      const result = await adminService.banUser(data);
     setIsChanged(result ? true : false);
    }catch (error) {
      ifServerThrowNavigate(error);
      setErrorNumberAdmin(convertErrorStringInNumber(error));
        const rawMessage = convertResponseMessage(error);
        messageOrFieldChecker(rawMessage);  
    }
    
  }

  const adminContextValues = {
    onChangeFindUserSubmit,
    onClickChangeRole,
    resetPage,
    setErrorMessageAdmin,
    setServerErrorsAdmin,
    setIsChanged,
    onClickBanUser,
    isChanged,
    serverErrorsAdmin,
    errorMessageAdmin,
    foundUserEmail: userInfo.email,
    foundUserFullName: userInfo.fullName,
    foundUserRole: userInfo.role,
    foundUserAge: userInfo.age,
  };

  return (
    <AdminContext.Provider value={adminContextValues}>
      {children}
    </AdminContext.Provider>
  );
};

export const useAdminContext = () => {
  const context = useContext(AdminContext);

  return context;
};
