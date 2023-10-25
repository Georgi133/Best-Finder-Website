import { useTranslation } from "react-i18next";
import { useAuthContext } from "../AuthContext/AuthContext";
import { ButtonSubmit } from "../Login/ButtonSubmit"
import { useForm } from "../useForm/useForm";
import style from './EditProfile.module.css'

export const EditingProfileWindow = ({
    setChangeInfo,
}) => {
  const { t }  = useTranslation();

  const {userFullName, userAge, userEmail, onEditSubmit } = useAuthContext();
  const { values , changeHandler, onSubmit } = useForm({
    newEmail: userEmail,
    age: userAge,
    fullName : userFullName ,
    email : userEmail,
  }, onEditSubmit);


    return(
        <div className={style.overlay}>
        <div className={style.backdrop}></div>
        <div className={style.modal}>
          <div className={style.detailContainer}>
            <button
              className={style.btn2}
              type="submit"
              onClick={() => setChangeInfo(false)}
            >
              X
            </button>
            <div className={style.content}>
              <div className={style.userDetails}>
                <h1 className={style.regenerationPass}>
                {t("userForm.profile")}
                </h1>

                <form className={style.formRegPass} method="post" onSubmit={onSubmit}>
                  <label className={style.emailLabel} htmlFor="newEmail">
                  {t("userForm.email")}:
                  </label>
                  <input
                  value={values.newEmail}
                    className={style.emailInp}
                    type="newEmail"
                    id="newEmail"
                    name="newEmail"
                    onChange={changeHandler}
                  />
                  <label className={style.emailLabel} htmlFor="fullName">
                  {t("userForm.fullName")}:
                  </label>
                  <input
                  value={values.fullName}
                    className={style.emailInp}
                    type="text"
                    id="fullName"
                    name="fullName"
                    onChange={changeHandler}
                  />
                   <label className={style.emailLabel} htmlFor="age">
                   {t("userForm.age")}:
                  </label>
                  <input
                    className={style.emailInp}
                    value={values.age}
                    type="age"
                    id="age"
                    name="age"
                    onChange={changeHandler}
                  />
                  
                  <button type="submit" className={style.editBtn}>
                  {t("userForm.editButton")}
          </button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    );

}