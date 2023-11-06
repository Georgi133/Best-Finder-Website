import { useTranslation } from "react-i18next";
import { useAuthContext } from "../AuthContext/AuthContext";
import { MyNavBar } from "../Header/MyNavBar";
import { useForm } from "../useForm/useForm";
import style from "./ChangePassword.module.css";
import { ButtonSubmit } from "../Login/ButtonSubmit";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { ErrorMessage } from "../ErrorMessage/ErrorMessage";
import { useNavigate } from "react-router-dom";

export const ChangePassword = () => {
  const { onChangePasswordSubmit, userEmail, serverErrors, errorMessage, changedPasswordSuccess, setChangedPasswordSuccess} = useAuthContext();
  const { validateChangePassword } = useValidatorContext();

  const { t } = useTranslation();
  const navigate = useNavigate();

  const { values, changeHandler, onSubmit, formErrors } = useForm(
    {
      currentPassword: "",
      newPassword: "",
      confirmPassword: "",
      email: userEmail,
    },
    onChangePasswordSubmit
  );

  const onChangePass = (e) => {
    onSubmit(e, validateChangePassword(values));
  }

  const redirectPage = () => {
    setTimeout(redirect,3000);

  }

  const redirect = () => {
    navigate('/');
    setChangedPasswordSuccess(false);
  }

  return (
    <>
      <MyNavBar />
      <div className={style.container}>
      <form className={style.formContainer} onSubmit={onChangePass}>
      {changedPasswordSuccess && <div id="reloadPage" className={style.success}>{t("successffully.success")}!</div>}
        {changedPasswordSuccess && redirectPage()}
        <div className={style.login + " " + "form-group"}>
        {errorMessage && <ErrorMessage message={errorMessage} />}
          <label className={style.lyrics} htmlFor="currentPassword">
          {t("changePasswordPage.currentPass")}:
          </label>
          <input
            value={values.currentPassword}
            onChange={changeHandler}
            type="password"
            className={style.inp + " form-control"}
            id="currentPassword"
            name="currentPassword"
            autoComplete="true"
            placeholder="Old Password"
            required
            min={4}
          />
          {formErrors.currentPassword ? <ErrorMessage message={formErrors.currentPassword}/> :
          serverErrors.currentPassword ? <ErrorMessage message={serverErrors.currentPassword}/>: ''}
        </div>
        <div className={style.login + " " + "form-group"}>
          <label className={style.lyrics} htmlFor="newPassword">
          {t("changePasswordPage.newPass")}:
          </label>
          <input
            value={values.newPassword}
            onChange={changeHandler}
            type="password"
            autoComplete="true"
            className={style.inp + " form-control"}
            id="newPassword"
            name="newPassword"
            placeholder="New Password"
            required
            min={4}
          />
          {formErrors.password ? <ErrorMessage message={formErrors.password}/> : 
          serverErrors.currentPassword ? <ErrorMessage message={serverErrors.newPassword}/> : ''}
        </div>
        <div className={style.login + " " + "form-group"}>
          <label className={style.lyrics} htmlFor="confirmPassword">
          {t("changePasswordPage.confirmNewPass")}:
          </label>
          <input
            value={values.confirmPassword}
            onChange={changeHandler}
            type="password"
            autoComplete="true"
            className={style.inp + " form-control"}
            id="confirmPassword"
            name="confirmPassword"
            placeholder="Confirm Password"
            required
            min={4}
          />
          {formErrors.password ? <ErrorMessage message={formErrors.password}/> : 
          serverErrors.currentPassword ? <ErrorMessage message={serverErrors.confirmPassword}/> : ''}
        </div>
        <ButtonSubmit/>
      </form>
      </div>
    </>
  );
};
