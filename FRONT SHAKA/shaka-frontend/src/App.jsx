import { useMemo, useState } from 'react'
import './App.css'

const API_URL = 'http://localhost:8080/api/v1/auth/register'

const initialFormState = {
  firstname: '',
  lastname: '',
  email: '',
  password: '',
  role: 'USER',
}

function App() {
  const [formData, setFormData] = useState(initialFormState)
  const [status, setStatus] = useState({ type: null, message: '', token: '' })
  const [isSubmitting, setIsSubmitting] = useState(false)

  const isSubmitDisabled = useMemo(() => {
    return (
      !formData.firstname.trim() ||
      !formData.lastname.trim() ||
      !formData.email.trim() ||
      !formData.password.trim()
    )
  }, [formData])

  const handleChange = (event) => {
    const { name, value } = event.target
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setIsSubmitting(true)
    setStatus({ type: null, message: '', token: '' })

    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      })

      if (!response.ok) {
        const errorPayload = await response.text()
        const errorMessage = errorPayload || `La solicitud falló con el estado ${response.status}`
        throw new Error(errorMessage)
      }

      const data = await response.json()
      const token = data?.access_token ?? ''

      if (token) {
        localStorage.setItem('authToken', token)
      }

      setStatus({
        type: 'success',
        message: 'Registro exitoso. El token se guardó para futuros pedidos.',
        token,
      })
      setFormData(initialFormState)
    } catch (error) {
      setStatus({
        type: 'error',
        message: error instanceof Error ? error.message : 'Ocurrió un error inesperado',
        token: '',
      })
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <main className="register-layout">
      <section className="register-card">
        <header>
          <h1>Crear cuenta</h1>
          <p className="subtitle">Completa el formulario para registrarte.</p>
        </header>

        <form className="register-form" onSubmit={handleSubmit}>
          <label className="field">
            <span>Nombre</span>
            <input
              type="text"
              name="firstname"
              value={formData.firstname}
              onChange={handleChange}
              placeholder="Ej: Ana"
              autoComplete="given-name"
              required
            />
          </label>

          <label className="field">
            <span>Apellido</span>
            <input
              type="text"
              name="lastname"
              value={formData.lastname}
              onChange={handleChange}
              placeholder="Ej: Pérez"
              autoComplete="family-name"
              required
            />
          </label>

          <label className="field">
            <span>Email</span>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="usuario@correo.com"
              autoComplete="email"
              required
            />
          </label>

          <label className="field">
            <span>Contraseña</span>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Mínimo 6 caracteres"
              autoComplete="new-password"
              required
            />
          </label>

          <label className="field">
            <span>Rol</span>
            <select name="role" value={formData.role} onChange={handleChange}>
              <option value="USER">Usuario</option>
              <option value="ADMIN">Administrador</option>
            </select>
          </label>

          <button type="submit" className="submit-button" disabled={isSubmitting || isSubmitDisabled}>
            {isSubmitting ? 'Registrando…' : 'Registrarme'}
          </button>
        </form>

        {status.type && (
          <div className={`status-message ${status.type}`}>
            <p>{status.message}</p>
            {status.token && (
              <code className="token">Token: {status.token}</code>
            )}
          </div>
        )}
      </section>

      <aside className="helper-panel">
        <h2>Detalles de la solicitud</h2>
        <div className="helper-content">
          <p>
            <strong>URL</strong>
          </p>
          <code>{API_URL}</code>
          <p>
            <strong>Body enviado</strong>
          </p>
          <pre>{JSON.stringify(formData, null, 2)}</pre>
          <p>
            <strong>Token guardado</strong>
          </p>
          <code>{localStorage.getItem('authToken') ?? 'N/A'}</code>
        </div>
      </aside>
    </main>
  )
}

export default App
