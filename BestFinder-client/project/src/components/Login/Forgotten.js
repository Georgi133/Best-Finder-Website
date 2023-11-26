import { useNavigate } from "react-router-dom";
import { useAuthContext } from "../AuthContext/AuthContext";
import { ErrorMessage } from "../ErrorMessage/ErrorMessage";
import { MyNavBar } from "../Header/MyNavBar";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { useForm } from "../useForm/useForm";
import { ButtonSubmit } from "./ButtonSubmit";
import style from "./Login.module.css";
import { useTranslation } from "react-i18next";
import { Footer } from "../Footer/Footer";

export const Forgotten = () => {
  const { t } = useTranslation();
  const { onPasswordRegeneration } = useValidatorContext();
  const { onForgottenPasswordEmail, errorMessage, serverErrors, successfullySendedPasswordOnEmail, setsuccessfullySendedPasswordOnEmail } = useAuthContext();

  const navigate = useNavigate();

  const { values, changeHandler, onSubmit, formErrors } = useForm(
    {
      userEmail: "",
    },
    onForgottenPasswordEmail
  );

  const onForgottenEmailSubmit = (e) => {
     onSubmit(e, onPasswordRegeneration(values));
    let input = document.getElementById('userEmail');
    input.value = '';
  };

  const redirectPage = () => {
    setTimeout(redirect,3000);

  }

  const redirect = () => {
    navigate('/users/login')
    setsuccessfullySendedPasswordOnEmail(false);
  }

  return (
    <>
      <MyNavBar />

    <div className={style.conainerForgotten}>
      <h1 className={style.regenerationPass2}>{t("passRecovery")}:</h1>

      <form className={style.forgottenForm} onSubmit={onForgottenEmailSubmit}>
        {successfullySendedPasswordOnEmail && <div id="reloadPage" className={style.success}>Successfully sended new password on the email!</div>}
        {successfullySendedPasswordOnEmail && redirectPage()}
      {errorMessage && <ErrorMessage message={errorMessage} />}
        <label className={style.emailLabel2} htmlFor="userEmail">
          {t("userForm.email")}:
        </label>
        <input
          value={values.email}
          onChange={changeHandler}
          className={style.emailInp}
          type="email"
          id="userEmail"
          name="userEmail"
          required
        />
        {formErrors.userEmail ? <ErrorMessage message={formErrors.userEmail}/> : serverErrors.userEmail ?
            <ErrorMessage message={serverErrors.userEmail}/> : ''}
        {/* //       : serverErrors.email ?
            // <ErrorMessage message={serverErrors.email}/> : ''} */}
        <button type="submit" className={style.btnForgotten + " btn btn-primary"}>
          {t("userForm.submit")}
          </button>
      </form>
    </div>
    <Footer />

    </>
  );
};
