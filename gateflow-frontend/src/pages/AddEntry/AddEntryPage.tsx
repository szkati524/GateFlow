import { useState } from "react";
import { useNavigate } from "react-router-dom"; 
import Header from "../../components/Common/Header";
import styles from './AddEntryPage.module.css';
import { apiFetch } from "../../api";

const AddEntryPage = () => {
    const navigate = useNavigate();
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    
    const [formData, setFormData] = useState({
        registration: '',
        brand: '',
        company: '',
        firstName: '',
        lastName: '',
        cargo: ''
    });

    const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

   const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    
    const payload = {
        registrationNumber: formData.registration,
        brand: formData.brand,
        companyName: formData.company,
        driverName: formData.firstName,
        driverSurname: formData.lastName,
        cargo: formData.cargo
    };

    try {
        const response = await apiFetch('/api/visits/entry', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload), 
        });

        if (response.ok) {
            alert("Pojazd dodany pomyślnie!");
            navigate('/'); 
        } else {
           
            const errorText = await response.text();
            console.error("Błąd serwera:", errorText);
            alert("Wystąpił błąd podczas dodawania wjazdu.");
        }
    } catch (error) {
        console.error("Błąd połączenia:", error);
        alert("Nie udało się połączyć z serwerem.");
    }
};
    const handleLogout = () => {
  
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    
    
    navigate('/login');
};

    return (
        <div className={styles.wrapper}>
            <Header />

            <div className={styles.topControls}>
                <button className={styles.menuTrigger} onClick={toggleSidebar}>
                    {isSidebarOpen ? '✕' : '☰'}
                </button>
                <div className={styles.rightActions}>
                    <button className={styles.iconBtn}>⚙️</button>
                   <button className={styles.logoutBtn} onClick={handleLogout}>Wyloguj się</button>
                </div>
            </div>

         
            <aside className={`${styles.sidebar} ${isSidebarOpen ? styles.sidebarOpen : ''}`}>
                <nav className={styles.navMenu}>
                    <a onClick={() => navigate("/")}>Ruch pojazdów</a>
                    <a onClick={() => navigate("/add-entry")}>Dodaj wjazd</a>
                    <a onClick={() => navigate("/search")} >Wyszukaj</a>
                    <a href="#Raport">Raporty</a>
                </nav>
            </aside>

            <main className={`${styles.content} ${isSidebarOpen ? styles.contentShifted : ''}`}>
                <div className={styles.formCard}>
                    <h2>Dodaj nowy wjazd</h2>
                    <form onSubmit={handleSubmit} className={styles.entryForm}>
                        <div className={styles.inputGroup}>
                            <label>Numer rejestracyjny</label>
                            <input type="text" name="registration" placeholder="np. KMY 12345" onChange={handleChange} required />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Marka pojazdu</label>
                            <input type="text" name="brand" placeholder="np. Scania" onChange={handleChange} />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Firma</label>
                            <input type="text" name="company" placeholder="Nazwa firmy" onChange={handleChange} />
                        </div>
                        <div className={styles.nameRow}>
                            <div className={styles.inputGroup}>
                                <label>Imię kierowcy</label>
                                <input type="text" name="firstName" placeholder="Imię" onChange={handleChange} />
                            </div>
                            <div className={styles.inputGroup}>
                                <label>Nazwisko kierowcy</label>
                                <input type="text" name="lastName" placeholder="Nazwisko" onChange={handleChange} />
                            </div>
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Ładunek</label>
                            <input type="text" name="cargo" placeholder="Co przewozi?" onChange={handleChange} />
                        </div>
                        <button type="submit" className={styles.submitBtn}>Zatwierdź wjazd</button>
                    </form>
                </div>
            </main>
        </div>
    );
};

export default AddEntryPage;