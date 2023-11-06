import { MyNavBar } from "../Header/MyNavBar";
import { ButtonSubmit } from "../Login/ButtonSubmit";
import style from "./Register.module.css";
import { useTranslation } from "react-i18next";
import { useForm } from "../useForm/useForm";
import { useAuthContext } from "../AuthContext/AuthContext";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { ErrorMessage } from "../ErrorMessage/ErrorMessage";
import { useNavigate } from "react-router-dom";

export const Register = () => {
  const { t } = useTranslation();
  const { validateRegister} = useValidatorContext();
  const navigate = useNavigate();

  const { onRegisterSubmit, errorMessage, serverErrors, registerSuccess, setRegisterSuccess } = useAuthContext();
  const { values, changeHandler, onSubmit, formErrors } = useForm(
    {
      email: "",
      password: "",
      fullName: "",
      age: '',
      confirmPassword: "",
    },
    onRegisterSubmit
  );

  const onSubmitForm  = (e) => {
    onSubmit(e,validateRegister(values));
  };

  const redirectPage = () => {
    setTimeout(redirect,3000);

  }

  const redirect = () => {
    navigate('/users/login');
    setRegisterSuccess(false);
  }


  return (
    <>
      <MyNavBar url={"register"} />
      <div className={style.container}>
        <form onSubmit={onSubmitForm} className={style.formContainer}>
        {registerSuccess && <div id="reloadPage" className={style.success}>{t("successffully.success")}!</div>}
        {registerSuccess && redirectPage()}
        {errorMessage && <ErrorMessage message={errorMessage} />}
          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="fullName">
              {t("userForm.fullName")}:
            </label>
            <input
              value={values.fullName}
              onChange={changeHandler}
              type="text"
              id="fullName"
              name="fullName"
              placeholder="Anatoli Bojinov"
              autoComplete="true"
              min={4}
              required
              className={style.inp + " form-control"}
            />
            {formErrors.fullName ? <ErrorMessage message={formErrors.fullName}/> : serverErrors.fullName ?
            <ErrorMessage message={serverErrors.fullName}/> : ''}
          </div>

          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="age">
              {t("userForm.age")}:
            </label>
            <input
              value={values.age}
              onChange={changeHandler}
              type="age"
              id="age"
              name="age"
              required
              placeholder="Age: 19"
              autoComplete="true"
              className={style.inp + " form-control"}
            />
            {formErrors.age ? <ErrorMessage message={formErrors.age}/> : serverErrors.age ?
            <ErrorMessage message={serverErrors.age}/> : ''}
          </div>
          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="email">
              {t("userForm.email")}:
            </label>
            <input
              value={values.email}
              onChange={changeHandler}
              type="email"
              name="email"
              autoComplete="true"
              className={style.inp + " form-control"}
              id="email"
              placeholder="Enter email"
              min={3}
              required
            />
            {formErrors.email ? <ErrorMessage message={formErrors.email}/> : serverErrors.email ?
            <ErrorMessage message={serverErrors.email}/> : ''}
          </div>
          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="password">
              {t("userForm.password")}:
            </label>
            <input
              value={values.password}
              onChange={changeHandler}
              type="password"
              className={style.inp + " form-control"}
              id="password"
              name="password"
              placeholder="Password"
              required
              autoComplete="true"
              min={4}
            />
            {formErrors.password ? <ErrorMessage message={formErrors.password}/> : serverErrors.password ?
            <ErrorMessage message={serverErrors.password}/> : ''}
          </div>
          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="confirmPassword">
              {t("userForm.confirmPassword")}:
            </label>
            <input
              value={values.confirmPassword}
              onChange={changeHandler}
              type="password"
              className={style.inp + " form-control"}
              id="confirmPassword"
              name="confirmPassword"
              placeholder="confirmPassword"
              required
              autoComplete="true"
              min={4}
            />
            {formErrors.password ? <ErrorMessage message={formErrors.password}/> : serverErrors.password ?
            <ErrorMessage message={serverErrors.password}/> : ''}
          </div>
          <div>
            <ButtonSubmit />
          </div>
        </form>
      </div>
    </>
  );
};
