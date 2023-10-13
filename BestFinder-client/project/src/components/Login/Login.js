import { MyNavBar } from "../Header/MyNavBar";
import style from "./Login.module.css";
import { useTranslation } from "react-i18next";
import { useState } from "react";
import { Forgotten } from "./Forgotten";
import { ButtonSubmit } from "./ButtonSubmit";
import { useAuthContext } from "../AuthContext/AuthContext";
import { useForm } from "../useForm/useForm"; 

export const Login = () => {
  const { onLoginSubmit } = useAuthContext();

  const [forgottenPass, setForgottenPass] = useState(false);
  const { t } = useTranslation();
  const { values , changeHandler, onSubmit } = useForm({
    email: '',
    password: '',
  }, onLoginSubmit);

  const onClickForgotten = () => {
    setForgottenPass(true);
  };

  return (
    <>
      <MyNavBar url={"login"} />
      {forgottenPass && <Forgotten setForgottenPass={setForgottenPass} />}
      <div className={style.container}>
        <form className={style.formContainer} onSubmit={onSubmit}>
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
              min={3}
            />
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
              min={3}
            />
          </div>

          <div
            className={
              style.login + " " + style.remember + " form-group" + " form-check"
            }
          >
            <a className={style.forgotten} onClick={onClickForgotten}>
              {t("userForm.forgotten")}?
            </a>
            <div className={style.check_submit}>
              <div className={style.remember_check}>
                <input
                  type="checkbox"
                  className={
                    style.checkbox + " " + style.inp + " form-check-input"
                  }
                  id="remember"
                />
                <label className="form-check-label" htmlFor="remember">
                  {t("userForm.remember")}
                </label>
              </div>
            </div>

            <ButtonSubmit/>
          </div>
        </form>
      </div>
    </>
  );
};
