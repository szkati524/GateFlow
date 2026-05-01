import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Common/Header";
import styles from './LoginPage.module.css';
import { apiFetch } from "../../api";

const LoginPage = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();


    useEffect(() => {
        if (localStorage.getItem("token")) {
            navigate("/");
        }
    }, [navigate]);

   const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    
    try {
        const response = await fetch("/api/auth/login", { 
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ 
                username: username, 
                password: password 
            })
        });

    if (response.ok) {
    const data = await response.json();
    console.log("SUKCES! Pełne dane z serwera:", data); 

    localStorage.setItem("token", data.token);

   
    localStorage.setItem("user", JSON.stringify(data)); 
    
    navigate("/");

        } else {
           
            const errorData = await response.text();
            console.error("Błąd logowania:", errorData);
            setError("Nieprawidłowy login lub hasło");
        }
    } catch (err) {
        setError("Błąd połączenia");
    }
};

    return (
        <>
            <Header />
            <div className={styles.pageContainer}>
                <div className={styles.loginBox}>
                    <h2>Zaloguj się do GateFlow</h2>
                    {error && <p className={styles.errorMessage}>{error}</p>}
                    <form onSubmit={handleLogin}>
                        <div className={styles.inputGroup}>
                            <label htmlFor="username">Nazwa użytkownika</label>
                            <input 
                                type="text" id="username" placeholder="login" 
                                value={username} onChange={(e) => setUsername(e.target.value)} 
                                required
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <label htmlFor="password">Hasło</label>
                            <input 
                                type="password" id="password" placeholder="hasło" 
                                value={password} onChange={(e) => setPassword(e.target.value)} 
                                required
                            />
                        </div>
                        <button type="submit" className={styles.loginButton}>
                            Zaloguj
                        </button>
                    </form>
                </div>
            </div>
        </>
    );
};

export default LoginPage;