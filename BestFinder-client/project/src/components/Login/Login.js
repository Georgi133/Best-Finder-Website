import { MyNavBar } from "../Header/MyNavBar";
import style from "./Login.module.css";
import { useTranslation } from "react-i18next";
import { ButtonSubmit } from "./ButtonSubmit";
import { useAuthContext } from "../AuthContext/AuthContext";
import { useForm } from "../useForm/useForm";
import { ErrorMessage } from "../ErrorMessage/ErrorMessage";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { Link } from "react-router-dom";
import { Footer } from "../Footer/Footer";

export const Login = () => {
  const { onLoginSubmit, errorMessage, serverErrors } = useAuthContext();
  const { validateLogin } = useValidatorContext();

  const { t } = useTranslation();
  const { values, changeHandler, onSubmit, formErrors } = useForm(
    {
      email: "",
      password: "",
    },
    onLoginSubmit
  );


  const onSubmitForm = (e) => {
    onSubmit(e,validateLogin(values));
  };


  return (
    <>
      <MyNavBar url={"login"} />
      <div className={style.container}>

        <form className={style.formContainer} onSubmit={onSubmitForm}>
          {errorMessage && <ErrorMessage message={errorMessage} />}
          <div className={style.login + " " + "form-group"}>
            <label className={style.lyrics} htmlFor="email">
              {t("userForm.email")}:
            </label>
            <input
              value={values.email}
              onChange={changeHandler}
              type="email"
              name="email"
              className={style.inp + " form-control"}
              id="email"
              placeholder="Enter email"
              required
              min={4}
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
              autoComplete="on"
              min={4}
            />
            {formErrors.password ? <ErrorMessage message={formErrors.password}/> : serverErrors.password ?
            <ErrorMessage message={serverErrors.password}/> : ''}
          </div>

          <div
            className={
              style.login + " " + style.remember + " form-group" + " form-check"
            }
          >
            <Link className={style.forgotten} to={'/users/forgotten'}>
              {t("userForm.forgotten")}?
            </Link>

            <ButtonSubmit />
          </div>
        </form>
      </div>
      <Footer />

    </>
  );
};
