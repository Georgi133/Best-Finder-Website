import { createContext, useContext, useState } from "react";
import { adminServiceFactory } from "../services/AdminService";
import { useAuthContext } from "../AuthContext/AuthContext";
import { useNavigate } from "react-router-dom";

export const AdminContext = createContext();


export const AdminProvider = ({
    children,
}) => {
    const navigate = useNavigate();
    const [userInfo, setUserInfo] = useState({});
    const adminService = adminServiceFactory();
    const { userRole } = useAuthContext();

    const onChangeFindUserSubmit = async (data) => {

        if(!data.currentUserRole) {
            data.currentUserRole = userRole;
        }
        const result = await adminService.findUserByEmail(data);
        setUserInfo(result);
    }

    const onTorrentSubmit = async (data) => {
        adminService.uploadTorrent(data);
        
        navigate("/")

    }

    const onClickChangeRole = async (data) => {
        const { setChangeRole, ...userInfo } = data;
        const result = await adminService.changeRole(userInfo);
        setUserInfo(result);
        setChangeRole(false);
    }

    const adminContextValues = {
        onChangeFindUserSubmit,
        onClickChangeRole,
        onTorrentSubmit,
        foundUserEmail: userInfo.email,
        foundUserFullName: userInfo.fullName,
        foundUserRole: userInfo.role,
        foundUserAge: userInfo.age,
    }

    return(
        <AdminContext.Provider value={adminContextValues}>
           {children}
        </AdminContext.Provider>

    )
}

export const useAdminContext = () => {
    const context = useContext(AdminContext);

    return context;
};