<script>
  import { goto } from '$app/navigation'

  import CarPicker from '$lib/components/vehicles/CarPicker.svelte'
  import { addVehicle } from '$lib/api/vehicles'
  import auth from '$lib/stores/userStore'

  let vehicleForm

  const add = async () => {
    try {
      const formData = new FormData(vehicleForm)
      const data = Object.fromEntries(formData)
      await addVehicle(data)
      auth.logout()
      goto('/login')
    } catch (err) {
      console.log(err)
    }
  }
</script>

<form
  bind:this={vehicleForm}
  on:submit|preventDefault={add}
  class="flex max-w-[444px] flex-col justify-between rounded-lg border bg-white py-10 px-10"
>
  <div>
    <h1>Register your vehicle</h1>
    <p class="mt-1">Enter information about your vehicle.</p>
    <div class="mt-4">
      <span class="text-sm font-medium">Make</span>
      <div class="mt-2">
        <CarPicker />
      </div>
    </div>
    <div class="mt-4">
      <label for="registration">Registration</label>
      <input
        required
        name="registration"
        placeholder="Enter your car registration"
        class="mt-1"
        type="text"
      />
    </div>
  </div>
  <button class="primary mt-10">Register vehicle</button>
</form>
