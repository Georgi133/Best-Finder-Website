import { useEffect, useState } from 'react';
import { useAuthContext } from '../AuthContext/AuthContext';
import { MyNavBar } from '../Header/MyNavBar';
import style from './EditProfile.module.css'
import { useTranslation } from 'react-i18next';
import { EditingProfileWindow } from './EditingProfileWindow';
import  jwt_decode  from 'jwt-decode'

import { Footer } from "../Footer/Footer";

export const EditProfile = () => {

    const { t }  = useTranslation();
    const [changeInfo , setChangeInfo] = useState(false);

    const {userFullName, userAge, userEmail, onProfileChange } = useAuthContext();

  const token = JSON.parse(localStorage.getItem("token"))


    useEffect(() => {
        if(token) {
            const decoded = jwt_decode(token);
            const {sub} = decoded;
            onProfileChange({email:sub});
          }
    
    }, [])

    const editProfile = () => {
        setChangeInfo(true);
    }

    return(
        <>
        <MyNavBar />
        {changeInfo && <EditingProfileWindow setChangeInfo={setChangeInfo} />}
        <div className={style.container}>
            <h2 className={style.headerInfo}>{t("userForm.profileOnEditPage")}</h2>
            <div className={style.innerContainer}> 
                <p>{t("userForm.email")}: <strong className={style.initials}>{userEmail}</strong></p>
                <p>{t("userForm.fullName")}: <strong className={style.initials}>{userFullName}</strong></p>
                <p>{t("userForm.age")}: <strong className={style.initials}>{userAge}</strong></p>
            </div>
            <button className={style.btn + " btn btn-primary"} type="submit" onClick={editProfile}>
            {t("userForm.editButton")}
            </button>
        </div>
        <Footer />

        </>

    );

}