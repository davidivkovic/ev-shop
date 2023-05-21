<script>
  import { goto } from '$app/navigation'
  import auth from '$lib/stores/userStore'
  import { addShop } from '$lib/api/shops'

  import CarPicker from '$lib/components/vehicles/CarPicker.svelte'

  let shopForm

  const add = async () => {
    try {
      const formData = new FormData(shopForm)
      const { name, address, ...others } = Object.fromEntries(formData)
      const brands = Object.values({ ...others })
      await addShop({ name, address, brands })
      auth.logout()
      goto('/login')
    } catch (err) {
      console.log(err)
    }
  }
</script>

<form
  bind:this={shopForm}
  on:submit|preventDefault={add}
  class="flex max-w-[444px] flex-col justify-between rounded-lg border bg-white py-10 px-10"
>
  <div>
    <h1>Register your shop</h1>
    <p class="mt-1">Enter information about your shop.</p>
    <div class="mt-5 flex flex-col gap-2">
      <label for="email">Name</label>
      <input required type="text" name="name" placeholder="Enter your shop's name" />
    </div>
    <div class="mt-5 flex flex-col gap-2">
      <label for="email">Address</label>
      <input
        required
        type="text"
        name="address"
        placeholder="Enter your shop's address"
        autocomplete="street-address"
      />
    </div>
    <div class="mt-4">
      <span class="text-sm font-medium">Brands you service</span>
      <div class="mt-2">
        <CarPicker multiple={true} />
      </div>
    </div>
  </div>
  <button class="primary mt-10">Register shop</button>
</form>
