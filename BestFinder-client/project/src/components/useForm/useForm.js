import { useEffect, useState } from "react";
import { useValidatorContext } from "../ValidatorContext/ValidatorContext";
import { useAuthContext } from "../AuthContext/AuthContext";
import { useAdminContext } from "../AdminContext/AdminContext";

export const useForm = (initialValues, onSubmitHandler) => {
  const { setErrorMessage, setServerErrors,setsuccessfullySendedPasswordOnEmail } = useAuthContext();
  
  const { setErrorMessageAdmin, setServerErrorsAdmin } = useAdminContext();
  const [values, setValues] = useState(initialValues);
  const [formErrors, setFormErrors] = useState({});

  const { valid, setValid } = useValidatorContext();

  useEffect(() => {
    if (valid) {
      setValues(initialValues);
      if(onSubmitHandler) {
        onSubmitHandler(values);
      }
    }
  }, [valid]);

  useEffect(() => {
    return () => {
      setValid(false);
    };
  });

  const changeHandler = (e) => {
    setValues((state) => ({ ...state, [e.target.name]: e.target.value }));
    setServerErrorsAdmin('');
    setErrorMessageAdmin('');
    setErrorMessage('');
    setServerErrors('');
    setFormErrors('');
    setsuccessfullySendedPasswordOnEmail(false);
  };

  const onSubmit = (e, validate) => {
    e.preventDefault();
    if (validate) {
      setFormErrors(validate);
    }
    // let input = document.getElementById("userEmail");
    // input.value = '';
  };

  const changeValues = (newValues) => {
    // TODO: Validate newValues shape (like initialValues)

    setValues(newValues);
  };

  return {
    valid,
    values,
    formErrors,
    changeHandler,
    onSubmit,
    changeValues,
  };
};
