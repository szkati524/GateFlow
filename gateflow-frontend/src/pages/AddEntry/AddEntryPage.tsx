import { useState } from "react";
import { useNavigate } from "react-router-dom"; 
import Navbar from "../../components/Common/Navbar";
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
            registrationNumber: formData.registration.trim(),
            brand: formData.brand.trim(),
            companyName: formData.company.trim(),
            driverName: formData.firstName.trim(),
            driverSurname: formData.lastName.trim(),
            cargo: formData.cargo.trim()
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

    return (
        <div className={styles.wrapper}>
            <Navbar isSidebarOpen={isSidebarOpen} onToggleSidebar={toggleSidebar} />

            <main className={`${styles.content} ${isSidebarOpen ? styles.contentShifted : ''}`}>
                <div className={styles.formCard}>
                    <h2>Dodaj nowy wjazd</h2>
                    <form onSubmit={handleSubmit} className={styles.entryForm}>
                        <div className={styles.inputGroup}>
                            <label>Numer rejestracyjny *</label>
                            <input 
                                type="text" 
                                name="registration" 
                                placeholder="np. KMY 12345" 
                                onChange={handleChange} 
                                required 
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Marka pojazdu *</label>
                            <input 
                                type="text" 
                                name="brand" 
                                placeholder="np. Scania" 
                                onChange={handleChange} 
                                required 
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Firma *</label>
                            <input 
                                type="text" 
                                name="company" 
                                placeholder="Nazwa firmy" 
                                onChange={handleChange} 
                                required 
                            />
                        </div>
                        <div className={styles.nameRow}>
                            <div className={styles.inputGroup}>
                                <label>Imię kierowcy *</label>
                                <input 
                                    type="text" 
                                    name="firstName" 
                                    placeholder="Imię" 
                                    onChange={handleChange} 
                                    required 
                                />
                            </div>
                            <div className={styles.inputGroup}>
                                <label>Nazwisko kierowcy *</label>
                                <input 
                                    type="text" 
                                    name="lastName" 
                                    placeholder="Nazwisko" 
                                    onChange={handleChange} 
                                    required 
                                />
                            </div>
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Ładunek *</label>
                            <input 
                                type="text" 
                                name="cargo" 
                                placeholder="Co przewozi?" 
                                onChange={handleChange} 
                                required 
                            />
                        </div>
                        <button type="submit" className={styles.submitBtn}>Zatwierdź wjazd</button>
                    </form>
                </div>
            </main>
        </div>
    );
};

export default AddEntryPage;