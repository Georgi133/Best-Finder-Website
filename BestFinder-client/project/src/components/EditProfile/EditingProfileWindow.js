import { useTranslation } from "react-i18next";
import { useAuthContext } from "../AuthContext/AuthContext";
import { useForm } from "../useForm/useForm";
import style from './EditProfile.module.css'
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { ErrorMessage } from "../ErrorMessage/ErrorMessage";
import { useNavigate } from "react-router-dom";

export const EditingProfileWindow = ({
    setChangeInfo,
}) => {
  const { t }  = useTranslation();
  const navigate = useNavigate();

  const {userFullName, userAge, userEmail, onEditSubmit, errorMessage, serverErrors, isProfileEdited, setIsProfileEdited } = useAuthContext();
  const { validateEditProfile } = useValidatorContext();
  const { values , changeHandler, onSubmit, formErrors } = useForm({
    age: userAge,
    fullName : userFullName ,
    email : userEmail,
  }, onEditSubmit);

  const onEdit = (e) => {
    onSubmit(e, validateEditProfile(values));
  }

  const redirectPage = () => {
    setTimeout(redirect,2000);
  }

  const redirect = () => {
    navigate('/');
    setIsProfileEdited(false)
  }


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
                {errorMessage && <ErrorMessage message={errorMessage} />}

                <form className={style.formRegPass} method="post" onSubmit={onEdit}>
                {isProfileEdited && <div id="reloadPage" className={style.success}>Successfully edited!</div>}
               {isProfileEdited && redirectPage()}
               
                  { !isProfileEdited && <label className={style.emailLabel} htmlFor="fullName">
                  <strong>{t("userForm.fullName")}:</strong>
                  </label>}
                { !isProfileEdited &&
                  <input
                  value={values.fullName}
                    className={style.emailInp}
                    type="text"
                    id="fullName"
                    name="fullName"
                    onChange={changeHandler}
                  />
                }
               
                  {formErrors.fullName ? <ErrorMessage message={formErrors.fullName}/> : 
                  serverErrors.fullName ? <ErrorMessage message={serverErrors.fullName}/>: ''}
                   { !isProfileEdited && <label className={style.emailLabel} htmlFor="age">
                   <strong>{t("userForm.age")}:</strong>
                  </label> }
                  {! isProfileEdited &&
                  <input
                    className={style.emailInp}
                    value={values.age}
                    type="age"
                    id="age"
                    name="age"
                    onChange={changeHandler}
                  />
                   }
                  {formErrors.age ? <ErrorMessage message={formErrors.age}/> : 
                  serverErrors.age ? <ErrorMessage message={serverErrors.age}/>:
                  ''}
                  
                  { !isProfileEdited && 
                  <button type="submit" className={style.editBtn}>
                  {t("userForm.editButton")}
          </button> }

                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    );

}