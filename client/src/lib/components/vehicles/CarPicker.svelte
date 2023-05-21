<script>
  export let multiple = false

  $: inputType = multiple ? 'checkbox' : 'radio'

  const makes = {
    Tesla: ['Model X', 'Model Y', 'Model 3', 'Model S'],
    Kia: ['Soul EV', 'Niro EV', 'EV6'],
    BMW: ['BMW i3', 'BMW i4', 'BMW iX3', 'BMW i8'],
    Audi: ['Q4 e-tron', 'e-tron GT', 'e-tron', 'e-tron Sportback']
  }

  let selectedMake = ''
</script>

<div class="grid grid-cols-2 grid-rows-2 gap-3">
  {#each Object.keys(makes) as make}
    <div>
      <input
        on:input={() => (selectedMake = make)}
        type={inputType}
        id={make}
        required={!multiple}
        name={multiple ? make : 'make'}
        class="peer hidden"
        value={make}
      />
      <label
        for={make}
        class="relative block aspect-square w-full cursor-pointer rounded-md border bg-white peer-checked:border-2 peer-checked:border-black"
      >
        <div class="relative h-full w-full">
          <img
            src={`/images/${make.toLowerCase()}.jpg`}
            class={`aspect-square object-contain ${make === 'Audi' && 'scale-75'}`}
            alt="Make of car"
          />
          <span class="absolute bottom-4 left-4">{make}</span>
        </div>

        <span class="absolute inset-0" />
      </label>
    </div>
  {/each}
</div>

{#if !multiple && selectedMake !== ''}
  <span class="mt-4 block text-sm font-semibold">Choose the model</span>
  <div class="mt-2 flex flex-col gap-1">
    {#each makes[selectedMake] as model}
      <div class="flex gap-2">
        <input required type="radio" id={model} name="model" value={model} />
        <label for={model}>{model}</label>
      </div>
    {/each}
  </div>
{/if}
